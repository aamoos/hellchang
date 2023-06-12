package com.hellzzang.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

/**
 * packageName    : com.hellzzangAdmin.entity
 * fileName       : BannerFile
 * author         : 김재성
 * date           : 2023-05-17
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-05-17        김재성       최초 생성
 */

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
public class BannerFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bannerId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id")
    private FileInfo fileInfo;

    @Builder
    public BannerFile(Long id, Long bannerId, FileInfo fileInfo){
        this.id = id;
        this.bannerId = bannerId;
        this.fileInfo = fileInfo;
    }

}
