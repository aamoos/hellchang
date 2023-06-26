package com.hellzzang.dto;

import com.hellzzang.entity.Comment;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * packageName    : com.hellzzang.dto
 * fileName       : CommentListDto
 * author         : hj
 * date           : 2023-06-15
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-06-15        hj       최초 생성
 */
@Data
public class CommentListDto {

    private Long commentId;

    private LocalDateTime commentDate;

    private String commentContent;

    private Comment parent;
    private List<CommentListDto> children;

    private String nickname;

    private Long userId;

    public CommentListDto(final Comment comment) {
        this.commentId = comment.getId();
        this.commentDate = comment.getCommentDate();
        this.commentContent = comment.getContent();
        this.parent = comment.getParentComment();
        this.children = comment.getChildComments().stream().map(CommentListDto::new).collect(Collectors.toList());
        this.nickname = comment.getUser().getNickname();
        this.userId = comment.getUser().getId();
    }
}
