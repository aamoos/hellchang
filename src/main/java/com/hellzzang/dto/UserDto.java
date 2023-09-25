package com.hellzzang.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hellzzang.entity.Community;
import com.hellzzang.entity.User;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
* @package : com.example.jwt.dto
* @name : UserDto.java
* @date : 2023-04-19 오후 5:26
* @author : hj
* @Description: 회원가입 시 사용
**/
@Data
@NoArgsConstructor
public class UserDto {

    @NotNull
    @Size(max = 50)
    private String userId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotEmpty(message = "비밀번호는 필수입력 값입니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*])[a-zA-Z\\d!@#$%^&*]{8,14}$", message = "비밀번호는 8~14글자 사이며 영어, 숫자, 특수문자 필수입니다.")
    private String password;

    @NotEmpty(message = "이름은 필수입력 값입니다.")
    @Pattern(regexp = "^([가-힣]{2,4}|[a-zA-Z]{2,10})\\s?([a-zA-Z]{2,10})?$", message = "한글 이름은 2~4글자, 영어 이름은 앞뒤 각각 2~10글자 작성해야 합니다.")
    private String userName;

    @NotEmpty(message = "닉네임은 필수입력 값입니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,12}$", message = "닉네임은 2~15글자 사이어야 합니다.")
    private String nickName;

    @NotEmpty(message = "주소는 필수입력 값입니다.")
    @Size(max = 50)
    private String address;

    @NotEmpty(message = "상세주소는 필수입력 값입니다.")
    @Size(max = 50)
    private String addressDetail;

    @NotEmpty(message = "번호는 필수입력 값입니다.")
    @Pattern(regexp = "^01[01][0-9]{7,8}$", message = "- 제외하여 입력해야 합니다.")
    private String phone;

    //권한
    private String authority;

    public User toEntity(){
        return User.builder()
                .userId(userId)
                .password(password)
                .userName(userName)
                .nickName(nickName)
                .address(address)
                .addressDetail(addressDetail)
                .phone(phone)
                .build();
    }
}