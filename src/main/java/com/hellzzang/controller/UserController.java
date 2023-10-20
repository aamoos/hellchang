package com.hellzzang.controller;

import com.hellzzang.common.ValidationErrorResponse;
import com.hellzzang.dto.UserDto;
import com.hellzzang.dto.myInfo.MyInfoResponseDto;
import com.hellzzang.dto.myInfo.UserEditRequestDto;
import com.hellzzang.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * packageName    : com.hellzzang.controller
 * fileName       : UserController
 * author         : 김재성
 * date           : 2023-10-20
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-10-20        김재성       최초 생성
 */

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    //내정보 조회
    @PostMapping("/getMyInfo")
    public MyInfoResponseDto getMyInfo(@RequestHeader(name="Authorization") String token){
        return userService.getMyInfo(token);
    }

    //내정보 수정
    @PostMapping("/edit")
    public ResponseEntity<?> getMyInfo(@Valid UserEditRequestDto userDto, BindingResult result
            , @RequestHeader(name="Authorization") String token
            , @RequestPart(value = "thumbnailFile", required = false) MultipartFile thumbnailFile,
                HttpServletRequest request){

        if (result.hasErrors()) {
            Map<String, List<String>> fieldErrors = new HashMap<>();
            System.out.println(result.getFieldErrors());
            result.getFieldErrors().forEach(fieldError -> {
                String field = fieldError.getField();
                String message = fieldError.getDefaultMessage();
                fieldErrors.computeIfAbsent(field, key -> new ArrayList<>()).add(message);
            });

            return ResponseEntity.badRequest().body(new ValidationErrorResponse(fieldErrors));
        }

        return ResponseEntity.ok(userService.userEdit(userDto, token, thumbnailFile, request));
    }

}
