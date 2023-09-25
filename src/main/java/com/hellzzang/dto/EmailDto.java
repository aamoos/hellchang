package com.hellzzang.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * packageName    : com.hellchang.dto
 * fileName       : MailDto
 * author         : hj
 * date           : 2023-05-03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-05-03        hj       최초 생성
 */
@Data
@NoArgsConstructor
public class EmailDto {

    @NotNull
    @Size(min = 3, max = 50)
    private String userId;

    @JsonIgnore
    @NotNull
    @Size(min = 15, max = 15)
    private String checkCode;

}
