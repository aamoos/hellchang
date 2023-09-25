package com.hellzzang.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * packageName    : com.hellzzang.dto
 * fileName       : DetailContent
 * author         : hj
 * date           : 2023-06-15
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-06-15        hj       최초 생성
 */
@Data
@NoArgsConstructor
public class DetailContentDto {

    private Long postId;

    private String title;

    private String content;

    private int likes;

    private LocalDateTime postDate;

    private String nickname;

    private Long userId;


    @QueryProjection
    public DetailContentDto(Long postId, String title, String content, int likes, LocalDateTime postDate, String nickname, Long userId){
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.likes = likes;
        this.postDate = postDate;
        this.nickname = nickname;
        this.userId = userId;
    }
}
