package com.hellzzang.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hellzzang.entity.Community;
import com.hellzzang.entity.Exercise;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * packageName    : com.hellzzang.dto
 * fileName       : CommunityDto
 * author         : 김재성
 * date           : 2023-09-19
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-09-19        김재성       최초 생성
 */

@Data
public class CommunityDto {

    private Long id;

    @NotEmpty(message = "제목은 필수입력 값입니다.")
    private String title;

    @Lob
    @Column(name = "text_area", columnDefinition = "CLOB")
    @NotEmpty(message = "내용은 필수입력 값입니다.")
    private String contents;

    public Community toEntity(){
        return Community.builder()
                .id(id)
                .title(title)
                .contents(contents)
                .build();
    }

}
