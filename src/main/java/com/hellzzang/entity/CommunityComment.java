package com.hellzzang.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
public class CommunityComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "community_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Community community;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    private CommunityComment parent;

    @Builder.Default
    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    @JsonIgnore
    private List<CommunityComment> children = new ArrayList<>();

    private boolean isElementVisible;

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

    public void defaultElementVisible(){
        this.isElementVisible = false;
    }
}
