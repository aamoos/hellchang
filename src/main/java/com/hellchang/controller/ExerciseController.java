package com.hellchang.controller;

import com.hellchang.entity.Exercise;
import com.hellchang.repository.ExerciseRepository;
import com.hellchang.service.ExerciseService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * packageName    : com.hellchang.controller
 * fileName       : ExcerciseController
 * author         : 김재성
 * date           : 2023-04-11
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-04-11        김재성       최초 생성
 */

@RestController
@RequiredArgsConstructor
@Slf4j
public class ExerciseController {

    private final ExerciseService exerciseService;
    private final ExerciseRepository exerciseRepository;

    @PostMapping("/exercise/list")
    public Result list(@RequestBody ExerciseDto exerciseDto){
        List<Exercise> list = exerciseRepository.findByExerciseDateAndDelYnOrderByIdAsc(exerciseDto.getExerciseDate(), "N");
        List<ExerciseListDto> collect = list.stream()
                .map(m -> new ExerciseListDto(m.getId(), m.getExerciseName(), m.getSetCount(), m.getKilogram(), m.getReps(), m.getDelYn(), m.getCompleteYn()))
                .collect(Collectors.toList());
        return new Result(collect.size(), collect);
    }

    @PostMapping("/exercise/save")
    public ResponseEntity<?> save(@RequestBody @Valid ExerciseDto exerciseDto, BindingResult result){
        log.info("bindingResult ={}", result);

        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
        }

        Exercise exercise = exerciseDto.toEntity();
        exerciseService.save(exercise);
        return ResponseEntity.status(HttpStatus.CREATED).body(exercise);
    }

    @Data
    static class ExerciseDto{
        private Long userId;                    //사용자 idx

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

        private String exerciseDate;            //운동날짜

        public Exercise toEntity(){
            return Exercise.builder()
                    .userId(userId)
                    .exerciseName(exerciseName)
                    .setCount(setCount)
                    .kilogram(kilogram)
                    .reps(reps)
                    .exerciseDate(exerciseDate)
                    .build();
        }
    }

    @Data
    @AllArgsConstructor
    static class ExerciseListDto{

        private Long id;

        private String exerciseName;            //운동명

        private int setCount;                   //세트

        private int kilogram;                   //kg

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

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data; // 리스트의 값
    }

}
