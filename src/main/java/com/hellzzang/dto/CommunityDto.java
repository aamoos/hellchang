package com.hellzzang.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hellzzang.entity.Community;
import com.hellzzang.entity.Exercise;
import com.hellzzang.entity.User;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommunityDto {

    private Long id;

    @NotEmpty(message = "제목은 필수입력 값입니다.")
    private String title;

    @Lob
    @Column(name = "text_area", columnDefinition = "CLOB")
    @NotEmpty(message = "내용은 필수입력 값입니다.")
    private String contents;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    private Long thumbnailIdx;

    private String delYn;

    private User user;

    public Community toEntity(){
        return Community.builder()
                .id(id)
                .title(title)
                .contents(contents)
                .user(user)
                .build();
    }

    @QueryProjection
    public CommunityDto(Long id, String title, String contents, LocalDateTime createdDate, LocalDateTime modifiedDate, Long thumbnailIdx, String delYn, User user) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.thumbnailIdx = thumbnailIdx;
        this.delYn = delYn;
        this.user = user;
    }

}
