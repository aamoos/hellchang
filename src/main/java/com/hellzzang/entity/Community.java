package com.hellzzang.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * packageName    : com.hellzzang.entity
 * fileName       : Community
 * author         : 김재성
 * date           : 2023-09-18
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-09-18        김재성       최초 생성
 */

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Community extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    @Column(name = "text_area", columnDefinition = "CLOB")
    private String contents;

    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommunityFile> files = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    private Long thumbnailIdx;

    private String delYn; //삭제여부

    //썸네일 idx 업데이트
    public void updateThumbnailIdx(Long thumbnailIdx){
        this.thumbnailIdx = thumbnailIdx;
    }

    @Builder
    public Community(Long id, String title, String contents, User user, Long thumbnailIdx, List<CommunityFile> files){
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.delYn = "N";
        this.user = user;
        this.thumbnailIdx = thumbnailIdx;
        this.files = files;
    }

}
