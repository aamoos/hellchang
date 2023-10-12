package com.hellzzang.dto;

import com.hellzzang.entity.Community;
import com.hellzzang.entity.FileInfo;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * packageName    : com.hellzzang.dto
 * fileName       : CommunityFileDto
 * author         : 김재성
 * date           : 2023-10-04
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-10-04        김재성       최초 생성
 */

@Data
@NoArgsConstructor
public class CommunityFileDto {

    private Long fileId;

    @QueryProjection
    public CommunityFileDto(Long fileId){
        this.fileId = fileId;
    }

}
