package com.hellzzang.service;


import com.hellzzang.dto.UserDto;
import com.hellzzang.entity.Authority;
import com.hellzzang.entity.Email;
import com.hellzzang.entity.User;
import com.hellzzang.exception.UserNotFoundException;
import com.hellzzang.repository.EmailRepository;
import com.hellzzang.repository.UserRepository;
import com.hellzzang.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final EmailRepository emailRepository;
    private final PasswordEncoder passwordEncoder;

    private final JavaMailSender mailSender;
    private static final String FROM_ADDRESS = "gidwns617@naver.com";
    private final SpringTemplateEngine thymeleafTemplateEngine;

    @Value("${app_url}")
    private String appUrl;

    /**
    * @methodName : signup
    * @date : 2023-04-20 오후 1:02
    * @author : hj
    * @Description: 회원가입 시 호출되는 메서드
    **/
    public User signup(UserDto userDto) {  //userId을 통해 이미 가입되어 있는지 확인
        if (userRepository.findOneWithAuthoritiesByuserId(userDto.getUserId()).orElse(null) != null) {
            // .orElse : optional에 들어갈 값이 null일 경우 orElse 안의 내용을 실행
            // Optional이란? 자바에서 Null 참조시 NullPointerException을 방지해주는 클래스
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }

        LocalDateTime currentDate = LocalDateTime.now();
        String blockDateFormat = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); //저장될 패턴
        currentDate = LocalDateTime.parse(blockDateFormat, dateFormatter); //String 데이터를 LocalDateTime 형태로 파싱

        Authority authority = new Authority("ROLE_USER");

        User user = User.builder() //유저 정보 빌드
                .userId(userDto.getUserId())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .userName(userDto.getUserName())  //이름
                .nickName(userDto.getNickName())
                .address(userDto.getAddress())
                .addressDetail(userDto.getAddressDetail())
                .phone(userDto.getPhone())
                .lastLoginDate(currentDate)
                .authorities(Collections.singleton(authority))
                .build();

        //동록된 이메일 인증코드 전부삭제
        emailRepository.deleteByuserId(userDto.getUserId());

        return userRepository.save(user);  // save = DB에 insert
    }

    /**
    * @methodName : sendEmail
    * @date : 2023-05-02 오후 4:46
    * @author : hj
    * @Description: 가입 요청 이메일 전송
    **/
    public void sendEmail(String userId) throws MessagingException, IOException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(userId);
        helper.setFrom(FROM_ADDRESS);
        helper.setSubject("<Hellzzang> 회원가입을 위해 이메일 인증을 진행해주세요.");

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
                .userId(userId)
                .checkCode(checkCode)
                .build();

        //회원가입 url
        String joinUrl = appUrl+"/userJoin/"+checkCode;

        thymeleafContext.setVariable("userId", userId);
        thymeleafContext.setVariable("joinUrl", joinUrl);
        emailRepository.save(email);

        // generate the HTML content from the Thymeleaf template
        String htmlContent = thymeleafTemplateEngine.process("email.html", thymeleafContext);

        helper.setText(htmlContent, true);
        mailSender.send(message);
        System.out.println("메일 전송 완료 ----------------------------------------");

    }

    /**
     * @methodName : emailCheck
     * @date : 2023-05-03 오후 5:22
     * @author : hj
     * @Description: 회원가입 시 부여된 랜덤 코드를 통해 유저 id 확인
     **/
    @Transactional(readOnly = true)
    public String emailCheck(String checkCode){
        Optional<Email> optionalEmail = emailRepository.findBycheckCode(checkCode);
        if (optionalEmail.isPresent()){
            Email email = optionalEmail.get();
            String userId = email.getUserId();
            System.out.println(" 코드에 대한 이메일 테이블에서 아이디 존재 ----------------");
            if (userRepository.findByuserId(userId) != null){
                System.out.println(" 코드에 대한 유저 테이블에서 아이디 존재 ------------------");
                return null;
            }
            else{
                System.out.println(" 코드에 대한 유저 테이블에서 아이디 존재하지 않음 ------------------");
                return userId;
            }
        } else {
            System.out.println("코드에 대한 아이디 존재하지 않음 -------------------");
            return null;
        }
    }

}