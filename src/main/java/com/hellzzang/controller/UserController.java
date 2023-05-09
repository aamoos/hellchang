package com.hellzzang.controller;

import com.hellzzang.dto.UserDto;
import com.hellzzang.entity.User;
import com.hellzzang.service.UserService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.util.Optional;

/**
* @package : com.example.jwt.controller
* @name : UserController.java
* @date : 2023-04-19 오후 5:18
* @author : hj
* @Description: User 관련 클래스
**/
@Slf4j
@Controller
@RequestMapping("/auth")
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
    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity<?> signup(@Valid @RequestBody UserDto userDto, BindingResult result) {
        log.info("bindingResult ={}", result);

        if (result.hasErrors()){
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

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
    @PostMapping("/userIdCheck")
    @ResponseBody
    public ResponseEntity<?> userIdCheck(@Valid @RequestBody userIdCheckDto request, BindingResult result){
        log.info("bindingResult ={}", result);

        if (result.hasErrors()){
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        return ResponseEntity.ok(userService.userIdCheck(request.getUserid()));
    }

    /**
    * @methodName : sendEmail
    * @date : 2023-05-02 오후 4:17
    * @author : hj
    * @Description: 이메일 전송 api, 로그인 시 해당 유저가 존재하지 않으면 해당 api 호출
    **/
    @PostMapping("/sendEmail")
    @ResponseBody
    public void sendEmail(@Valid @RequestBody userIdCheckDto request) throws MessagingException, IOException {
        userService.sendEmail(request.getUserid());
    }

    /**
    * @methodName : emailCheck
    * @date : 2023-05-03 오후 5:22
    * @author : hj
    * @Description: 회원가입 시 부여된 랜덤 코드를 통해 유저 id 확인
    **/
    @PostMapping("/emailCheck")
    @ResponseBody
    public ResponseEntity<String> emailCheck(@Valid @RequestBody mailCheckDto request){
        return ResponseEntity.ok(userService.emailCheck(request.getCheckcode()));
    }

    /**
    * @methodName : nicknameCheck
    * @date : 2023-05-03 오전 11:20
    * @author : hj
    * @Description: 닉네임 중복검사
    **/
    @PostMapping("/nicknameCheck")
    @ResponseBody
    public ResponseEntity<Boolean> nicknameCheck(@Valid @RequestBody nicknameCheckDto request){
        return ResponseEntity.ok(userService.nicknameCheck(request.getNickname()));
    }

    /**
     * @methodName : userIndexCheck
     * @date : 2023-05-09 오후 4:02
     * @author : hj
     * @Description: 회원 index 번호 비교하여 회원정보 불러옴
     **/
    @PostMapping("/editProfile")
    @ResponseBody
    public ResponseEntity<Optional<User>> userIndexCheck(@RequestBody userIndexDto request){
        return ResponseEntity.ok(userService.userIndexCheck(request.getId()));
    }

    /**
    * @methodName : userChangeInfo
    * @date : 2023-05-09 오후 5:51
    * @author : hj
    * @Description: 유저 정보 변경
    **/
    @PostMapping("/userChangeInfo")
    @ResponseBody
    public ResponseEntity<?> userChangeInfo(@Valid @RequestBody userInfoChangeDto request){
        userService.userChangeInfo(request.getId(), request.getAddress(), request.getAddressDetail(), request.getPhone());
        return ResponseEntity.ok("200");
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
    * @Description: 유저 아이디 관련 Dto
    **/
    @Data
    static class userIdCheckDto{

        @NotBlank(message = "아이디를 입력하세요.")
        @NotNull
        @Pattern(regexp = "^((admin)|(lhj))$|^([\\w.%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,})$", message = "이메일 형식이 올바르지 않습니다.")
        private String userid;
    }

    /**
    * @methodName : nicknameCheckDto
    * @date : 2023-05-03 오전 11:20
    * @author : hj
    * @Description: 닉네임 중복검사 Dto
    **/
    @Data
    static class nicknameCheckDto{

        @NotNull
        private String nickname;
    }

    /**
    * @methodName : mailCheck
    * @date : 2023-05-03 오후 5:21
    * @author : hj
    * @Description: 회원가입 시 랜덤 코드를 통해 아이디 확인
    **/
    @Data
    static class mailCheckDto{

        @NotNull
        private String checkcode;
    }
    
    /**
    * @methodName : userIndexDto
    * @date : 2023-05-09 오후 3:53
    * @author : hj
    * @Description: 회원 번호
    **/
    @Data
    static class userIndexDto{

        @NotNull
        private Long id;
    }

    /**
    * @methodName : userInfoChangeDto
    * @date : 2023-05-09 오후 5:51
    * @author : hj
    * @Description: 유저 정보 변경 Dto
    **/
    @Data
    static class userInfoChangeDto{

        @NotNull
        private Long id;

        @NotBlank(message = "주소는 필수입력 값입니다.")
        @Size(max = 50)
        private String address;

        @NotBlank(message = "상세주소는 필수입력 값입니다.")
        @Size(max = 50)
        private String addressDetail;

        @NotNull(message = "번호는 필수입력 값입니다.")
        @Pattern(regexp = "^01[01][0-9]{7,8}$", message = "전화번호 형식이 올바르지 않습니다.(-제외하고 입력)")
        private String phone;
    }
}


