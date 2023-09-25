package com.hellzzang.service;

import com.hellzzang.dto.ExerciseDto;
import com.hellzzang.dto.QExerciseDto;
import com.hellzzang.entity.Exercise;
import com.hellzzang.repository.ExerciseRepository;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import static com.hellzzang.entity.QExercise.exercise;

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
@Transactional
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;

    private final JPAQueryFactory jpaQueryFactory;

    private final JPAQueryFactory queryFactory;

    /**
    * @methodName : save
    * @date : 2023-04-19 오후 5:14
    * @author : 김재성
    * @Description: 운동계획 저장
    **/
    public Exercise save(Exercise exercise){
        return exerciseRepository.save(exercise);
    }

    public void delete(LocalDate exerciseDate){
        exerciseRepository.deleteByExerciseDate(exerciseDate);
    }

    /**
    * @methodName : updateCompleteYn
    * @date : 2023-04-19 오후 5:14
    * @author : 김재성
    * @Description: 운동계획 완료여부 업데이트 
    **/
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
    * @methodName : copy
    * @date : 2023-04-21 오후 2:22
    * @author : 김재성
    * @Description: 복사기능
    **/
    public void copy(LocalDate startDate, LocalDate endDate, LocalDate targetDate){
        List<Exercise> list = exerciseRepository.findByExerciseDateAndDelYn(targetDate, "N");
        //ex) startDate: 2023-05-03 endDate: 2023-05-06
        // 선택한 날짜의 데이터를 3일부터 6일까지 데이터를 복사
        for (LocalDate date = startDate; date.isBefore(endDate.plusDays(1)); date = date.plusDays(1)) {
            int index = 0;
            for (Exercise exercise : list) {
                Exercise item = exercise.builder()
                        .userId(list.get(index).getUserId())
                        .exerciseName(list.get(index).getExerciseName())
                        .setCount(list.get(index).getSetCount())
                        .kilogram(list.get(index).getKilogram())
                        .reps(list.get(index).getReps())
                        .delYn(list.get(index).getDelYn())
                        .completeYn(list.get(index).getCompleteYn())
                        .exerciseDate(date)
                        .build();
                //insert
                exerciseRepository.save(item);
                index++;
            }
        }
    }

    public void move(LocalDate moveDate, LocalDate targetDate){

        List<Exercise> exercises = exerciseRepository.findByExerciseDateAndDelYn(targetDate, "N");

        for (Exercise exercise : exercises) {
            exercise.updateExerciseDate(moveDate);
        }

        exerciseRepository.saveAll(exercises);
    }

    /**
    * @methodName : updateDelYn
    * @date : 2023-04-19 오후 5:15
    * @author : 김재성
    * @Description: 운동계획 삭제하기
    **/
    public void updateDelYn(LocalDate exerciseDate){

        List<Exercise> exercises = exerciseRepository.findByExerciseDateAndDelYn(exerciseDate, "N");

        for (Exercise exercise : exercises) {
            exercise.updateDelYn("Y");
        }
        exerciseRepository.saveAll(exercises);
    }

    /**
    * @methodName : statistics
    * @date : 2023-05-03 오후 4:21
    * @author : 김재성
    * @Description: 이번주 운동 통계 조회
    **/
    public Map<LocalDate, Long> statistics(Long id) {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.next(DayOfWeek.SUNDAY)).minusWeeks(1);
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        List<Tuple> tuples = queryFactory
                .select(exercise.exerciseDate, exercise.id.count())
                .from(exercise)
                .where(exercise.exerciseDate.between(startOfWeek, endOfWeek))       //이번 일주일간의 데이터 조회
                .where(exercise.userId.eq(id)) 
                .where(exercise.completeYn.eq("Y"))                            //운동완료
                .where(exercise.delYn.eq("N"))                                 //삭제가 안된항목
                .groupBy(exercise.exerciseDate)
                .fetch();

        Map<LocalDate, Long> result = tuples.stream().collect(Collectors.toMap(
                t -> LocalDate.from(t.get(exercise.exerciseDate)),
                t -> t.get(exercise.id.count())
        ));

        for (LocalDate date = startOfWeek; date.isBefore(endOfWeek.plusDays(1)); date = date.plusDays(1)) {
            result.putIfAbsent(date, 0L);
        }

        return result;
    }

    /**
    * @methodName : getCalendarData
    * @date : 2023-05-31 오후 2:48
    * @author : 김재성
    * @Description: 달력 데이터 가져오기
    **/
    public List<ExerciseDto> getCalendarData(Long id){

        List<ExerciseDto> content = jpaQueryFactory
                .select(new QExerciseDto(
                    exercise.exerciseDate
                ))
                .distinct()
                .from(exercise)
                .where(exercise.userId.eq(id))
                .where(exercise.delYn.eq("N"))
                .fetch();
        return content;
    }

}
