package com.hellzzang.dto.myInfo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * packageName    : com.hellzzang.dto.myInfo
 * fileName       : UserEditRequestDto
 * author         : 김재성
 * date           : 2023-10-20
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-10-20        김재성       최초 생성
 */

@Data
public class UserEditRequestDto {

    @NotEmpty(message = "이름은 필수입력 값입니다.")
    @Pattern(regexp = "^([가-힣]{2,4}|[a-zA-Z]{2,10})\\s?([a-zA-Z]{2,10})?$", message = "한글 이름은 2~4글자, 영어 이름은 앞뒤 각각 2~10글자 작성해야 합니다.")
    private String userName;

    @NotEmpty(message = "닉네임은 필수입력 값입니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,12}$", message = "닉네임은 2~15글자 사이어야 합니다.")
    private String nickName;

    @NotEmpty(message = "주소는 필수입력 값입니다.")
    private String address;

    @NotEmpty(message = "상세주소는 필수입력 값입니다.")
    private String addressDetail;

    @Pattern(regexp = "^01[01][0-9]{7,8}$", message = "- 제외하여 입력해야 합니다.")
    @NotEmpty(message = "번호는 필수입력 값입니다.")
    private String phone;
}
