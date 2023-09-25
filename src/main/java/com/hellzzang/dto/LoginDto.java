package com.hellzzang.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
* @package : com.example.jwt.dto
* @name : LoginDto.java
* @date : 2023-04-19 오후 5:19
* @author : hj
* @Description: 토큰 발급 시 사용
**/
@Data
@NoArgsConstructor
public class LoginDto {

    @NotBlank(message = "아이디를 입력하세요.")
    private String userId;

    @NotNull
    @Size(min = 3, max = 100)
    private String password;
}