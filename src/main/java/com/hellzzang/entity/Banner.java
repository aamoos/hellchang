package com.hellzzang.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * packageName    : com.hellzzangAdmin.entity
 * fileName       : Banner
 * author         : 김재성
 * date           : 2023-05-16
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-05-16        김재성       최초 생성
 */

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Banner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bannerPath;

    private String delYn;

    private Long fileTotal;

    @CreatedDate
    private String createdDate;

    @LastModifiedDate
    private String modifiedDate;

    @PrePersist
    public void onPrePersist(){
        this.createdDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.modifiedDate = this.createdDate;
    }

    @PreUpdate
    public void onPreUpdate(){
        this.modifiedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_index")
    private AdminUsers adminUsers;

    @Builder
    public Banner(Long id, String bannerPath, Long fileTotal, AdminUsers adminUsers, String createdDate){
        this.id = id;
        this.bannerPath = bannerPath;
        this.delYn = "N";
        this.fileTotal = fileTotal;
        this.adminUsers = adminUsers;
        this.createdDate = createdDate;
    }

    public Banner delete(){
        this.delYn = "Y";
        return this;
    }

}
