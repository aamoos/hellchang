package com.hellchang.service;

import com.hellchang.controller.ExerciseController;
import com.hellchang.entity.Exercise;
import com.hellchang.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * packageName    : com.hellchang.service
 * fileName       : ExerciseService
 * author         : 김재성
 * date           : 2023-04-11
 * description    : 운동 계획 서비스
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-04-11        김재성       최초 생성
 */

@Service
@RequiredArgsConstructor
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;
    
    /**
    * @methodName : save
    * @date : 2023-04-19 오후 5:14
    * @author : 김재성
    * @Description: 운동계획 저장
    **/
    @Transactional
    public Exercise save(Exercise exercise){
        return exerciseRepository.save(exercise);
    }

    /**
    * @methodName : updateCompleteYn
    * @date : 2023-04-19 오후 5:14
    * @author : 김재성
    * @Description: 운동계획 완료여부 업데이트 
    **/
    @Transactional
    public void updateCompleteYn(Long id, String completeYn){
        Optional<Exercise> optionalExercise = exerciseRepository.findById(id);
        if(optionalExercise.isPresent()){
            Exercise exercise = optionalExercise.get();
            //완료여부 업데이트
            exercise.updateCompleteYn(completeYn);
        }else{
            throw new EntityNotFoundException("해당 운동을 찾을수 없습니다.");
        }
    }

    /**
    * @methodName : updateDelYn
    * @date : 2023-04-19 오후 5:15
    * @author : 김재성
    * @Description: 운동계획 삭제하기
    **/
    @Transactional
    public void updateDelYn(String exerciseDate, String delYn){

        List<Exercise> exercises = exerciseRepository.findByExerciseDate(exerciseDate);

        for (Exercise exercise : exercises) {
            exercise.updateDelYn("Y");
        }
        exerciseRepository.saveAll(exercises);
    }

}
