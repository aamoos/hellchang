package com.hellchang.dto;

import com.hellchang.entity.Exercise;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * packageName    : com.hellchang.dto
 * fileName       : ExerciseDto
 * author         : 김재성
 * date           : 2023-04-11
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-04-11        김재성       최초 생성
 */

@Getter
@Setter
public class ExerciseDto {

    @NotNull(message = "운동명은 필수 입력 값입니다.")
    private String exerciseName;            //운동명

    @Min(value = 1, message = "세트는 최소 1개 이상이어야 합니다.")
    private int setCount;                   //세트

    @Min(value = 1, message = "kg은 최소 1kg 이상이어야 합니다.")
    private int kilogram;                   //kg

    @Min(value = 1, message = "횟수는 최소 1회 이상이어야 합니다.")
    private int reps;                       //회

    private String delYn;                   //삭제여부

    private String completeYn;              //완료여부

    public Exercise toEntity(){
        return Exercise.builder()
                .exerciseName(exerciseName)
                .setCount(setCount)
                .kilogram(kilogram)
                .reps(reps)
                .build();
    }

}
