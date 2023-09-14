package com.hellzzang.controller;

import com.hellzzang.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/email")
public class EmailController {

    private final UserService userService;

    /**
     * @methodName : sendEmail
     * @date : 2023-05-02 오후 4:17
     * @author : hj
     * @Description: 이메일 전송 api, 로그인 시 해당 유저가 존재하지 않으면 해당 api 호출
     **/
    @PostMapping("/sendEmail")
    @ResponseBody
    public void sendEmail(@Valid @RequestBody UserController.userIdCheckDto request) throws MessagingException, IOException {
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
    public ResponseEntity<String> emailCheck(@Valid @RequestBody UserController.mailCheckDto request){
        return ResponseEntity.ok(userService.emailCheck(request.getCheckcode()));
    }

}
