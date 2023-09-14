package com.hellzzang.controller;

import com.hellzzang.common.ValidationErrorResponse;
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
import javax.validation.constraints.*;
import java.io.IOException;
import java.util.*;

/**
* @package : com.example.jwt.controller
* @name : UserController.java
* @date : 2023-04-19 오후 5:18
* @author : hj
* @Description: User 관련 클래스
**/
@Slf4j
@Controller

public class UserController {
    private final UserService userService;

    private AuthController authController;
    public UserController(UserService userService) {
        this.userService = userService;
    }


    /**
     * @methodName : userIndexCheck
     * @date : 2023-05-09 오후 4:02
     * @author : hj
     * @Description: 회원 index 번호 비교하여 회원정보 불러옴
     **/
    @PostMapping("/userInfo")
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
        userService.userChangeInfo(request.getId(), request.getAddress(), request.getAddressDetail(), request.getPhone(), request.getThumbnailIdx());
        return ResponseEntity.ok("200");
    }

    /**
     * @methodName : userCheckPassword
     * @date : 2023-05-11 오전 10:17
     * @author : hj
     * @Description: 유저의 index에 해당하는 비밀번호를 비교하여 존재하면 비밀번호 변경
     **/
    @PostMapping("/userChangePassword")
    @ResponseBody
    public ResponseEntity<String> userChangePassword(@Valid @RequestBody userPasswordCheckDto request){
        return ResponseEntity.ok(userService.userChangePassword(request.getId(), request.getOldPassword(), request.getNewPassword()));
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
        @Pattern(regexp = "^(admin)$|^([\\w.%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,})$", message = "이메일 형식이 올바르지 않습니다.")
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

        private Long thumbnailIdx;
    }

    /**
    * @package : com.hellzzang.controller
    * @name : UserController.java
    * @date : 2023-05-11 오후 1:30
    * @author : hj
    * @Description: 비밀번호 변경 Dto
    **/
    @Data
    static class userPasswordCheckDto{

        @NotNull
        private Long id;
        
        @NotNull
        @NotEmpty(message = "기존 비밀번호를 입력하세요.")
        private String oldPassword;

        @NotNull
        @NotEmpty(message = "변경하실 비밀번호를 입력하세요.")
        @Pattern(regexp = "^(admin)$|^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*])[a-zA-Z\\d!@#$%^&*]{8,14}$", message = "비밀번호는 8~14글자 사이며 영어, 숫자, 특수문자 필수입니다.")
        private String newPassword;
    }
}


