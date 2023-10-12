package com.hellzzang.entity;

import com.hellzzang.dto.CommunityCommentDto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CommunityComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "community_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Community community;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private CommunityComment parent;

    @Builder.Default
    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<CommunityComment> children = new ArrayList<>();

    // 부모 댓글 수정
    public void update(CommunityCommentDto communityCommentDto) {
        this.content = communityCommentDto.getContent();
    }

    // 부모 댓글 수정
    public void updateParent(CommunityComment parent){
        this.parent = parent;
    }

    public boolean validateMember(User user) {
        return !this.user.equals(user);
    }
}
