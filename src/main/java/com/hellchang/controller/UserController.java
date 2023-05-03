package com.hellchang.controller;

import com.hellchang.dto.UserDto;
import com.hellchang.entity.User;
import com.hellchang.service.UserService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
* @package : com.example.jwt.controller
* @name : UserController.java
* @date : 2023-04-19 오후 5:18
* @author : hj
* @Description: User 관련 클래스
**/
@Controller
public class UserController {
    private final UserService userService;

    private AuthController authController;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
    * @methodName : signup
    * @date : 2023-04-19 오후 5:18
    * @author : hj
    * @Description: 회원가입 메서드
    **/
    @PostMapping("/auth/signup")
    @ResponseBody
    public ResponseEntity<User> signup(
            @Valid @RequestBody UserDto userDto
    ) {
        return ResponseEntity.ok(userService.signup(userDto)); //http의 body, header, status를 포함한 데이터 -> 추가 서칭 필요
        //Response header에는 웹서버가 웹브라우저에 응답하는 메시지가 들어있음
        //Reponse body에는 데이터 값이 들어있음
    }
    
    /**
    * @methodName : userIdCheck
    * @date : 2023-05-02 오후 3:23
    * @author : hj
    * @Description: userid입력 후 로그인 시 로그인 or 회원가입
    **/
    @PostMapping("/auth/userIdCheck")
    @ResponseBody
    public ResponseEntity<Boolean> userIdCheck(@Valid @RequestBody userIdCheckDto request){
        return ResponseEntity.ok(userService.userIdCheck(request.getUserid()));
    }



//    @PostMapping("/userJoin")
//    @ResponseBody
//    public ResponseEntity<User> setUserId(@Valid @RequestBody userIdCheckDto request){
//        return ResponseEntity.ok(userService.setUserId(request.getUserid()));
//    }


    /**
    * @methodName : nicknameCheck
    * @date : 2023-05-03 오전 11:20
    * @author : hj
    * @Description: 닉네임 중복검사
    **/
    @PostMapping("/auth/nicknameCheck")
    @ResponseBody
    public ResponseEntity<Boolean> nicknameCheck(@Valid @RequestBody nicknameCheckDto request){
        return ResponseEntity.ok(userService.nicknameCheck(request.getNickname()));
    }

    /**
    * @methodName : sendEmail
    * @date : 2023-05-02 오후 4:17
    * @author : hj
    * @Description: 이메일 전송 api, 로그인 시 해당 유저가 존재하지 않으면 해당 api 호출
    **/
    @PostMapping("/auth/sendEmail")
    @ResponseBody
    public void sendEmail(@Valid @RequestBody userIdCheckDto request) throws MessagingException {
        userService.sendEmail(request.getUserid());
    }

    /**
    * @methodName : getMyUserInfo
    * @date : 2023-04-19 오후 5:18
    * @author : hj
    * @Description: 권한에 따라 User 정보 출력
    **/
    @GetMapping("/user")
    @ResponseBody
    @PreAuthorize("hasAnyRole('USER','ADMIN')") //user와 admin 모두 호출 가능하게 설정
    public ResponseEntity<User> getMyUserInfo() {  //Security Context에 저장되어 있는 인증 정보의 userid을 기준으로 한 유저정보 및 권한정보를 리턴
        return ResponseEntity.ok(userService.getMyUserWithAuthorities().get());
    }

    @GetMapping("/user/{userid}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ADMIN')")  //admin 권한만 호출 가능
    public ResponseEntity<User> getUserInfo(@PathVariable String userid) {  //userid 파라미터를 통해 해당 유저의 정보 및 권한 정보 리턴
        return ResponseEntity.ok(userService.getUserWithAuthorities(userid).get());
    }
    
    /**
    * @methodName : userIdCheckDto
    * @date : 2023-05-02 오후 3:49
    * @author : hj
    * @Description: 유저 아이디 관련
    **/
    @Data
    static class userIdCheckDto{

        @NotNull
        private String userid;
    }
    
    /**
    * @methodName : nicknameCheckDto
    * @date : 2023-05-03 오전 11:20
    * @author : hj
    * @Description: 닉네임 중복검사
    **/
    @Data
    static class nicknameCheckDto{

        @NotNull
        private String nickname;
    }
}


