package com.hellchang.repository;

import com.hellchang.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * packageName    : com.hellchang.repository
 * fileName       : ExcerciseRepository
 * author         : 김재성
 * date           : 2023-04-11
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-04-11        김재성       최초 생성
 */
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    /**
    * @methodName : findByExerciseDateAndDelYnOrderByIdAsc
    * @date : 2023-04-19 오후 5:22
    * @author : 김재성
    * @Description: 운동계획 리스트 조회
    **/
    List<Exercise> findByUserIdAndExerciseDateAndDelYnOrderByIdAsc(Long userId, LocalDate exerciseDate, String delYn);

    /**
    * @methodName : findByExerciseDate
    * @date : 2023-04-19 오후 5:22
    * @author : 김재성
    * @Description: 해당 날자에 등록된 운동 리스트 조회
    **/
    List<Exercise> findByExerciseDateAndDelYn(LocalDate exerciseDate, String delYn);

    /**
    * @methodName : deleteByExerciseDate
    * @date : 2023-04-24 오후 1:41
    * @author : 김재성
    * @Description: 해당 운동날짜에 등록된 데이터 전부 삭제
    **/
    void deleteByExerciseDate(LocalDate exerciseDate);
}
