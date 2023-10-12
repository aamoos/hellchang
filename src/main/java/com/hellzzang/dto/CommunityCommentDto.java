package com.hellzzang.dto;

import com.hellzzang.entity.Community;
import com.hellzzang.entity.CommunityComment;
import com.hellzzang.entity.User;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityCommentDto {


    private Long id;

    private User user;

    private Community community;

    private CommunityComment parent;

    @Builder.Default
    private List<CommunityComment> children = new ArrayList<>();

    private Long communityId;

    private Long parentId;

    @NotEmpty(message = "댓글은 필수입력 값입니다.")
    private String content;

    private int page;
    private int size;

    @QueryProjection
    public CommunityCommentDto(Long id, User user
            , Community community, String content
            , CommunityComment parent, List<CommunityComment> children ){
        this.id = id;
        this.user = user;
        this.community = community;
        this.content = content;
        this.parent = parent;
        this.children = children;
    }
}
