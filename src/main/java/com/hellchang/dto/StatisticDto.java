package com.hellchang.dto;

import lombok.Data;

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
public class StatisticDto {
    private String exerciseDate;            //운동날짜
    private int count;                      //count
}
