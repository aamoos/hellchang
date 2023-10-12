package com.hellzzang.dto;

import com.hellzzang.entity.Community;
import com.hellzzang.entity.CommunityFile;
import com.hellzzang.entity.User;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Column;
import javax.persistence.Lob;
import javax.validation.constraints.NotEmpty;
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
@NoArgsConstructor
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

    @Builder
    public CommunityDto(Community community){
        this.id = community.getId();
        this.title = community.getTitle();
        this.contents = community.getContents();
        this.delYn = community.getDelYn();
        this.user = community.getUser();
        this.thumbnailIdx = community.getThumbnailIdx();
    }

}
