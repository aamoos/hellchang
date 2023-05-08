package com.hellzzang.service;


import com.hellzzang.dto.UserDto;
import com.hellzzang.entity.Authority;
import com.hellzzang.entity.Email;
import com.hellzzang.entity.User;
import com.hellzzang.repository.EmailRepository;
import com.hellzzang.repository.UserRepository;
import com.hellzzang.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final EmailRepository emailRepository;
    private final PasswordEncoder passwordEncoder;

    private final JavaMailSender mailSender;
    private static final String FROM_ADDRESS = "gidwns617@naver.com";
    private final SpringTemplateEngine thymeleafTemplateEngine;

    /**
    * @methodName : signup
    * @date : 2023-04-20 오후 1:02
    * @author : hj
    * @Description: 회원가입 시 호출되는 메서드
    **/
    @Transactional
    public User signup(UserDto userDto) {  //userId을 통해 이미 가입되어 있는지 확인
        if (userRepository.findOneWithAuthoritiesByUserid(userDto.getUserid()).orElse(null) != null) {
            // .orElse : optional에 들어갈 값이 null일 경우 orElse 안의 내용을 실행
            // Optional이란? 자바에서 Null 참조시 NullPointerException을 방지해주는 클래스
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }

        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")  //권한을 USER 설정
                .build();

        User user = User.builder() //유저 정보 빌드
                .userid(userDto.getUserid())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .username(userDto.getUsername())  //이름
                .nickname(userDto.getNickname())
                  .address(userDto.getAddress())
                .phone(userDto.getPhone())
                .authorities(Collections.singleton(authority))
                .activated(true)
                .build();

        return userRepository.save(user);  // save = DB에 insert
    }

    /**
    * @methodName : userIdCheck
    * @date : 2023-05-02 오후 3:24
    * @author : hj
    * @Description: 로그인 시 userId 체크 후 회원 존재 시 메인 이동, 없으면 회원가입 이동
    **/
    @Transactional
    public boolean userIdCheck(String userid){
        if (userRepository.findByUserid(userid) != null){
            System.out.println("유저 존재 ------------------------------------------");
            return true;
        } else {
            System.out.println("유저 없음 -----------------------------------------");
            return false;
        }
    }

    /**
    * @methodName : sendEmail
    * @date : 2023-05-02 오후 4:46
    * @author : hj
    * @Description: 가입 요청 이메일 전송
    **/
    @Transactional
    public void sendEmail(String userid) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(userid);
        helper.setFrom(FROM_ADDRESS);
        helper.setSubject("회원가입을 위해 이메일 인증을 진행해주세요.");

        // create the Thymeleaf context object and add the name variable
        Context thymeleafContext = new Context();

        //유저 확인 코드 생성
        String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"; // 영어 대문자와 숫자
        int CODE_LENGTH = 15;

        Random random = new Random();
        StringBuilder code = new StringBuilder(CODE_LENGTH);

        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        System.out.println(code);
        String checkCode = code.toString();

        Email email = Email.builder()
                .userid(userid)
                .checkcode(checkCode)
                .build();

        thymeleafContext.setVariable("checkcode", checkCode);
        emailRepository.save(email);

        // generate the HTML content from the Thymeleaf template
        String htmlContent = thymeleafTemplateEngine.process("email.html", thymeleafContext);

        helper.setText(htmlContent, true);
        mailSender.send(message);
        System.out.println("메일 전송 완료 ----------------------------------------");

    }
    private String getHtmlContent(String htmlPath) throws IOException {
        ClassPathResource resource = new ClassPathResource(htmlPath);
        byte[] bytes = Files.readAllBytes(Paths.get(resource.getURI()));
        return new String(bytes, StandardCharsets.UTF_8);
    }

    /**
     * @methodName : emailCheck
     * @date : 2023-05-03 오후 5:22
     * @author : hj
     * @Description: 회원가입 시 부여된 랜덤 코드를 통해 유저 id 확인
     **/
    @Transactional
    public String emailCheck(String checkcode){
        Optional<Email> optionalEmail = emailRepository.findByCheckcode(checkcode);
        if (optionalEmail.isPresent()){
            Email email = optionalEmail.get();
            String userid = email.getUserid();
            System.out.println(" 코드에 대한 이메일 테이블에서 아이디 존재 ----------------");
            if (userRepository.findByUserid(userid) != null){
                System.out.println(" 코드에 대한 유저 테이블에서 아이디 존재 ------------------");
                return null;
            }
            else{
                System.out.println(" 코드에 대한 유저 테이블에서 아이디 존재하지 않음 ------------------");
                return userid;
            }
        } else {
            System.out.println("코드에 대한 아이디 존재하지 않음 -------------------");
            return null;
        }
    }

    /**
    * @methodName : nicknameCheck
    * @date : 2023-05-03 오전 11:21
    * @author : hj
    * @Description: 유저 닉네임 중복검사
    **/
    @Transactional
    public boolean nicknameCheck(String nickname){
        if (userRepository.findByNickname(nickname) != null){
            return true;
        } else {
            return false;
        }
    }

    /**
    * @methodName : getMyUserWithAuthorities
    * @date : 2023-04-20 오후 1:02
    * @author : hj
    * @Description: SecurityUtil의 getCurrentUserId 메소드가 리턴하는 userId 유저 권한 및 권한 정보 리턴
    **/
    @Transactional(readOnly = true)
    public Optional<User> getMyUserWithAuthorities() {
        return SecurityUtil.getCurrentUserId().flatMap(userRepository::findOneWithAuthoritiesByUserid);
    }

    /**
    * @methodName : getUserWithAuthorities
    * @date : 2023-04-20 오후 1:02
    * @author : hj
    * @Description: userId을 통해 해당 유저의 정보 및 권한 정보 리턴
    **/
    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities(String userId) {
        return userRepository.findOneWithAuthoritiesByUserid(userId);
    }

}