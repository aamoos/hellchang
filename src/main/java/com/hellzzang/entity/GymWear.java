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
import java.util.ArrayList;
import java.util.List;

/**
 * packageName    : com.hellzzangAdmin.entity
 * fileName       : GymWear
 * author         : 김재성
 * date           : 2023-05-22
 * description    : 짐웨어 entity
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-05-22        김재성       최초 생성
 */

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GymWear extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    @Column(name = "text_area", columnDefinition = "CLOB")
    private String contents;

    private String contentsText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_index")
    private AdminUsers adminUsers;

    @OneToMany(mappedBy = "gymWear", cascade = CascadeType.ALL)
    private List<GymWearFile> gymWearFiles = new ArrayList<>();

    private String optionYn;

    @OneToMany(mappedBy = "gymWear", cascade = CascadeType.ALL)
    private List<GymWearOption> gymWearOptions = new ArrayList<>();

    //짐웨어 파일 추가하기
    public void addGymWearFile(GymWearFile gymWearFile){
        if (this.gymWearFiles == null) {
            this.gymWearFiles = new ArrayList<>();
        }
        this.gymWearFiles.add(gymWearFile);
    }

    //짐웨어 옵션 추가하기
    public void addGymWearOption(GymWearOption gymWearOption){
        if (this.gymWearOptions == null) {
            this.gymWearOptions = new ArrayList<>();
        }
        this.gymWearOptions.add(gymWearOption);
    }

    private Long thumbnailIdx;

    private String delYn;

    private Long price;

    @Builder
    public GymWear(Long id, String title, String contents, String contentsText, AdminUsers adminUsers, Long thumbnailIdx,
                   Long price, List<GymWearFile> gymWearFiles, String optionYn, List<GymWearOption> gymWearOptions){
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.contentsText = contentsText;
        this.adminUsers = adminUsers;
        this.thumbnailIdx = thumbnailIdx;
        this.delYn = "N";
        this.price = price;
        this.gymWearFiles = gymWearFiles;
        this.optionYn = optionYn;
        this.gymWearOptions = gymWearOptions;
    }

    public GymWear delete(){
        this.delYn = "Y";
        return this;
    }

}
