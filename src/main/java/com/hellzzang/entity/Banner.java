package com.hellzzang.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Banner extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bannerPath;

    private String delYn;

    private Long fileTotal;

    @OneToMany(mappedBy = "banner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BannerFile> bannerFiles = new ArrayList<>();

    //배너파일 추가
    public void addBannerFiles(BannerFile bannerFile){
        if (this.bannerFiles == null) {
            this.bannerFiles = new ArrayList<>();
        }
        this.bannerFiles.add(bannerFile);
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_index")
    private AdminUsers adminUsers;

    @Builder
    public Banner(Long id, String bannerPath, Long fileTotal, AdminUsers adminUsers){
        this.id = id;
        this.bannerPath = bannerPath;
        this.delYn = "N";
        this.fileTotal = fileTotal;
        this.adminUsers = adminUsers;
    }

    public Banner delete(){
        this.delYn = "Y";
        return this;
    }

}
