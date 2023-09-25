package com.hellzzang.dto;

import com.hellzzang.entity.Banner;
import com.hellzzang.entity.FileInfo;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

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
@NoArgsConstructor
public class BannerFileDto {

    private Long id;

    private FileInfo fileInfo;

    @QueryProjection
    public BannerFileDto(Long id, FileInfo fileInfo) {
        this.id = id;
        this.fileInfo = fileInfo;
    }

}
