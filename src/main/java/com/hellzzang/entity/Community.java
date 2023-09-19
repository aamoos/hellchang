package com.hellzzang.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
@EntityListeners(AuditingEntityListener.class)
public class Community {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    @Column(name = "text_area", columnDefinition = "CLOB")
    private String contents;

    private String contentsText;

    @CreatedDate
    private String createdDate;

    @LastModifiedDate
    private String modifiedDate;

    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL)
    private List<CommunityFile> communityFiles = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    //커뮤니티 파일 추가하기
    public void addCommunityFile(CommunityFile communityFile){
        if (this.communityFiles == null) {
            this.communityFiles = new ArrayList<>();
        }
        this.communityFiles.add(communityFile);
    }
    
    @PrePersist
    public void onPrePersist(){
        this.createdDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.modifiedDate = this.createdDate;
    }

    @PreUpdate
    public void onPreUpdate(){
        this.modifiedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private Long thumbnailIdx;

    private String delYn;
    
}
