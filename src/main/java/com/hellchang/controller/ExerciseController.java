package com.hellchang.controller;

import com.hellchang.common.Result;
import com.hellchang.common.ValidList;
import com.hellchang.entity.Exercise;
import com.hellchang.repository.ExerciseRepository;
import com.hellchang.service.ExerciseService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
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

    /**
    * @methodName : list
    * @date : 2023-04-19 오후 4:14
    * @author : 김재성
    * @Description: 오늘의 운동 리스트 조회
    **/
    @PostMapping("/exercise/list")
    public Result list(@RequestBody FindRequestDto findRequestDto){
        //삭제 안된 운동조회
        List<Exercise> list = exerciseRepository.findByExerciseDateAndDelYnOrderByIdAsc(findRequestDto.getExerciseDate(), "N");
        List<ExerciseListDto> collect = list.stream()
                .map(m -> new ExerciseListDto(m.getId(), m.getExerciseName(), m.getSetCount(), m.getKilogram(), m.getReps(), m.getDelYn(), m.getCompleteYn()))
                .collect(Collectors.toList());
        return new Result(collect.size(), collect);
    }

    /**
    * @methodName : save
    * @date : 2023-04-19 오후 4:14
    * @author : 김재성
    * @Description: 오늘의 운동 저장
    **/
    @PostMapping("/exercise/save")
    public ResponseEntity<?> save(@Valid @RequestBody ValidList<SaveRequestItem> request, BindingResult result){
        log.info("bindingResult ={}", result);

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        for (SaveRequestItem item : request.getList()) {
            Exercise exercise = item.toEntity();
            exerciseService.save(exercise);
        }

        return ResponseEntity.ok("200");
    }

    /**
    * @methodName : updateCompleteYn
    * @date : 2023-04-19 오후 4:58
    * @author : 김재성
    * @Description: 완료여부 업데이트
    **/
    @PostMapping("/exercise/updateCompleteYn")
    public ResponseEntity<?> updateCompleteYn(@Valid @RequestBody UpdateCompleteYnRequest request, BindingResult result){
        log.info("bindingResult ={}", result);

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        //완료여부 업데이트
        exerciseService.updateCompleteYn(request.getId(), request.completeYn);

        return ResponseEntity.ok("200");
    }

    /**
    * @methodName :
    * @date : 2023-04-19 오후 4:58
    * @author : 김재성
    * @Description: 삭제여부 업데이트
    **/
    @PostMapping("/exercise/updateDelYn")
    public ResponseEntity<?> updateDeleteYn(@Valid @RequestBody UpdateDeleteYnRequest request, BindingResult result){
        log.info("bindingResult ={}", result);

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        //삭제처리하기
        exerciseService.updateDelYn(request.getExerciseDate(), request.delYn);

        return ResponseEntity.ok("200");
    }

    /**
    * @methodName :
    * @date : 2023-04-19 오후 4:14
    * @author : 김재성
    * @Description: 오늘의 운동 requestDto
    **/
    @Data
    static class FindRequestDto{
        private String exerciseDate;            //운동날짜
    }

    /**
     * @methodName :
     * @date : 2023-04-19 오후 4:15
     * @author : 김재성
     * @Description: 오늘의 운동 리스트 dto
     **/
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
    }

    /**
    * @methodName :
    * @date : 2023-04-19 오후 4:15
    * @author : 김재성
    * @Description: 오늘의 운동 저장 requestDto
    **/
    @Data
    static class SaveRequestItem{
        private Long userId;                    //사용자 idx

        @NotNull(message = "운동명은 필수 입력 값입니다.")
        private String exerciseName;            //운동명

        @NotNull(message = "세트는 최소 1개 이상이어야 합니다.")
        @Min(value = 1, message = "세트는 최소 1개 이상이어야 합니다.")
        private int setCount;                   //세트

        @NotNull(message = "kg은 최소 1kg 이상이어야 합니다.")
        @Min(value = 1, message = "kg은 최소 1kg 이상이어야 합니다.")
        private int kilogram;                   //kg

        @NotNull(message = "횟수는 최소 1회 이상이어야 합니다.")
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

    /**
    * @methodName :
    * @date : 2023-04-19 오후 5:31
    * @author : 김재성
    * @Description: 완료여부 request dto
    **/
    @Data
    static class UpdateCompleteYnRequest{

        @NotNull(message = "id는 필수 입력값 입니다.")
        private Long id;

        @NotNull(message = "완료여부는 필수 입력값 입니다.")
        private String completeYn;
    }

    /**
    * @methodName :
    * @date : 2023-04-19 오후 5:32
    * @author : 김재성
    * @Description: 삭제여부 request dto
    **/
    @Data
    static class UpdateDeleteYnRequest{

        @NotNull(message = "운동날짜는 필수값입니다.")
        private String exerciseDate;        //운동날짜

        @NotNull(message = "삭제여부는 필수 입력값 입니다.")
        private String delYn;               //삭제여부
    }

}
