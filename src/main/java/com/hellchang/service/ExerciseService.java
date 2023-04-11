package com.hellchang.service;

import com.hellchang.entity.Exercise;
import com.hellchang.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * packageName    : com.hellchang.service
 * fileName       : ExerciseService
 * author         : 김재성
 * date           : 2023-04-11
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-04-11        김재성       최초 생성
 */

@Service
@RequiredArgsConstructor
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;

    @Transactional
    public Exercise save(Exercise exercise){
        return exerciseRepository.save(exercise);
    }

}
