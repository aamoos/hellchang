package com.hellzzang.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * packageName    : com.hellzzangAdmin.dto
 * fileName       : BannerDto
 * author         : 김재성
 * date           : 2023-05-16
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-05-16        김재성       최초 생성
 */

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GymWearFileDto {

    @Id
    @GeneratedValue
    private Long id;

    private String contentType;

    private String delYn;

    private String extension;

    private String originFileName;

    private LocalDateTime regDate;

    private String savedFileName;

    private Long size;

    private String uploadDir;

    @QueryProjection
    public GymWearFileDto(Long id, String delYn, String extension, String originFileName, LocalDateTime regDate, String savedFileName, Long size, String uploadDir) {
        this.id = id;
        this.delYn = delYn;
        this.extension = extension;
        this.originFileName = originFileName;
        this.regDate = regDate;
        this.savedFileName = savedFileName;
        this.size = size;
        this.uploadDir = uploadDir;
    }

}
