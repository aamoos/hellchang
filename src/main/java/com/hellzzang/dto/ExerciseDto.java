package com.hellzzang.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

/**
 * packageName    : com.hellzzang.dto
 * fileName       : CalendarDto
 * author         : 김재성
 * date           : 2023-05-31
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-05-31        김재성       최초 생성
 */

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExerciseDto {

    private LocalDate exerciseDate;

    @QueryProjection
    public ExerciseDto(LocalDate exerciseDate) {
        this.exerciseDate = exerciseDate;
    }

}
