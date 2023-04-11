package com.hellchang.controller;

import com.hellchang.dto.ExerciseDto;
import com.hellchang.entity.Exercise;
import com.hellchang.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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

    @PostMapping("/insertExercise")
    public ResponseEntity<?>  save(@RequestBody @Valid ExerciseDto exerciseDto, BindingResult result){
        log.info("bindingResult ={}", result);

        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
        }

        Exercise exercise = exerciseDto.toEntity();
        exerciseService.save(exercise);
        return ResponseEntity.status(HttpStatus.CREATED).body(exercise);
    }


}
