package com.hellzzang.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * packageName    : com.hellchang.dto
 * fileName       : StatisticDto
 * author         : 김재성
 * date           : 2023-05-02
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-05-02        김재성       최초 생성
 */

@Data
@NoArgsConstructor
public class StatisticDto {
    private String exerciseDate;            //운동날짜
    private int count;                      //count
}
