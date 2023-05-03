package com.hellchang.service;


import com.hellchang.dto.UserDto;
import com.hellchang.entity.Authority;
import com.hellchang.entity.User;
import com.hellchang.repository.UserRepository;
import com.hellchang.utils.SecurityUtil;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Collections;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final JavaMailSender mailSender;
    private static final String FROM_ADDRESS = "gidwns617@naver.com";

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

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
            System.out.println("true ------------------------------------------");
            return true;
        } else {
            System.out.println("false -----------------------------------------");
            return false;
        }
    }

//    @Transactional
//    public User setUserId(String userid){
//
//    }

    /**
    * @methodName : nicknameCheck
    * @date : 2023-05-03 오전 11:21
    * @author : hj
    * @Description: 유저 닉네임 중복검사
    **/
    @Transactional
    public boolean nicknameCheck(String nickname){
        if (userRepository.findByNickname(nickname) != null){
            System.out.println("true ------------------------------------------");
            return true;
        } else {
            System.out.println("false -----------------------------------------");
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
    public void sendEmail(String userid) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(userid);
        helper.setFrom(FROM_ADDRESS);
        helper.setSubject("회원가입을 위해 이메일 인증을 진행해주세요.");
        helper.setText("http://localhost:8080/userJoin 으로 접속하여 회원가입을 진행해주세요.", true);
//        String htmlContent = "<p>" + mailDto.getMessage() +"<p> <img src='cid:sample-img'>";
        String htmlContent = "<div>"
                + "<a href='http://localhost:8080/userJoin' target='_blank' value='"+ userid + "' style='font-weight: bold; font-size:20px;'>회원가입</a>"
                + "으로 접속하여 회원가입을 진행해주세요."
                +"</div>";
        helper.setText(htmlContent, true);
        mailSender.send(message);
        System.out.println("메일 전송 완료 ----------------------------------------");
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