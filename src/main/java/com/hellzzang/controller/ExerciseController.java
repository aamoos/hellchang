package com.hellzzang.controller;

import com.hellzzang.common.Result;
import com.hellzzang.common.ValidList;
import com.hellzzang.dto.ExerciseDto;
import com.hellzzang.entity.Exercise;
import com.hellzzang.jwt.TokenProvider;
import com.hellzzang.repository.ExerciseRepository;
import com.hellzzang.service.ExerciseService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
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
@RequestMapping("/exercise")
public class ExerciseController {

    private final ExerciseService exerciseService;
    private final ExerciseRepository exerciseRepository;
    private final TokenProvider tokenProvider;

    /**
    * @methodName : list
    * @date : 2023-04-19 오후 4:14
    * @author : 김재성
    * @Description: 오늘의 운동 리스트 조회
    **/
    @PostMapping("/list")
    public Result list(@RequestBody FindRequestDto findRequestDto, @RequestHeader(name="Authorization") String token) throws Exception {
        System.out.println("token : " + token);
        //삭제 안된 운동조회
        List<Exercise> list = exerciseRepository.findByuserIdAndExerciseDateAndDelYnOrderByIdAsc(tokenProvider.getJwtTokenId(token), findRequestDto.getExerciseDate(), "N");
        List<ExerciseListDto> collect = list.stream()
                .map(m -> new ExerciseListDto(m.getId(), m.getExerciseName(), m.getSetCount(), m.getKilogram(), m.getReps(), m.getDelYn(), m.getCompleteYn()))
                .collect(Collectors.toList());
        return new Result(collect.size(), collect);
    }

    @PostMapping("/getCalendarData")
    public Result getCalendarData(@RequestHeader(name="Authorization") String token) throws Exception {
        System.out.println("token : " + token);

        //삭제 안된 운동조회
        List<ExerciseDto> collect = exerciseService.getCalendarData(tokenProvider.getJwtTokenId(token));

        //삭제 안된 운동조회
//        return exerciseService.getCalendarData(tokenProvider.getJwtTokenId(token));


        return new Result(collect.size(), collect);
    }

    /**
    * @methodName : save
    * @date : 2023-04-19 오후 4:14
    * @author : 김재성
    * @Description: 오늘의 운동 저장
    **/
    @PostMapping("/save")
    public ResponseEntity<?> save(@Valid @RequestBody ValidList<SaveRequestItem> request, BindingResult result) throws Exception {
        log.info("bindingResult ={}", result);

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        //해당 날짜 데이터 삭제
        if(request.getList().size() > 0){
            exerciseService.delete(request.get(0).getExerciseDate());

            //등록한 데이터들 저장
            for (SaveRequestItem item : request.getList()) {
                Exercise exercise = item.toEntity();
                exerciseService.save(exercise);
            }
        }else{
            throw new EntityNotFoundException("해당 운동을 찾을수 없습니다.");
        }

        return ResponseEntity.ok("200");
    }

    /**
    * @methodName : copy
    * @date : 2023-04-21 오전 11:08
    * @author : 김재성
    * @Description: 복사하기 api
    **/
    @PostMapping("/copy")
    public ResponseEntity<?> copy(@Valid @RequestBody CopyRequest request, BindingResult result){
        log.info("bindingResult ={}", result);

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        //복사하기
        exerciseService.copy(request.getStartDate(), request.getEndDate(), request.getTargetDate());

        return ResponseEntity.ok("200");
    }

    /**
    * @methodName : move
    * @date : 2023-04-24 오전 10:21
    * @author : 김재성
    * @Description: 
    **/
    @PostMapping("/move")
    public ResponseEntity<?> move(@Valid @RequestBody MoveRequest request, BindingResult result){
        log.info("bindingResult ={}", result);

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        //이동하기
        exerciseService.move(request.getMoveDate(), request.getTargetDate());

        return ResponseEntity.ok("200");
    }

    /**
    * @methodName : updateCompleteYn
    * @date : 2023-04-19 오후 4:58
    * @author : 김재성
    * @Description: 완료여부 업데이트
    **/
    @PostMapping("/updateCompleteYn")
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
    @PostMapping("/updateDelYn")
    public ResponseEntity<?> updateDeleteYn(@Valid @RequestBody UpdateDeleteYnRequest request, BindingResult result){
        log.info("bindingResult ={}", result);

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        //삭제처리하기
        exerciseService.updateDelYn(request.getExerciseDate());

        return ResponseEntity.ok("200");
    }

    /**
    * @methodName : statistics
    * @date : 2023-05-03 오후 4:21
    * @author : 김재성
    * @Description: 이번주 운동 통계 조회
    **/
    @PostMapping("/statistics")
    public Map<LocalDate, Long> statistics(@RequestHeader(name="Authorization") String token) throws Exception {
        return exerciseService.statistics(tokenProvider.getJwtTokenId(token));
    }

    /**
    * @methodName :
    * @date : 2023-04-19 오후 4:14
    * @author : 김재성
    * @Description: 오늘의 운동 requestDto
    **/
    @Data
    static class FindRequestDto{
        private LocalDate exerciseDate;            //운동날짜
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

        @NotBlank(message = "운동명은 필수 입력 값입니다.")
        private String exerciseName;            //운동명

        @Min(value = 1, message = "세트는 최소 1개 이상이어야 합니다.")
        private int setCount;                   //세트

        @Min(value = 1, message = "kg은 최소 1kg 이상이어야 합니다.")
        private int kilogram;                   //kg

        @Min(value = 1, message = "횟수는 최소 1회 이상이어야 합니다.")
        private int reps;                       //회

        private String delYn;                   //삭제여부

        private String completeYn;              //완료여부

        private LocalDate exerciseDate;            //운동날짜

        public Exercise toEntity(){
            return Exercise.builder()
                    .userId(userId)
                    .exerciseName(exerciseName)
                    .setCount(setCount)
                    .kilogram(kilogram)
                    .reps(reps)
                    .exerciseDate(exerciseDate)
                    .delYn(delYn)
                    .completeYn(completeYn)
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

        private Long id;

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

        private LocalDate exerciseDate;        //운동날짜

        private String delYn;               //삭제여부
    }

    /**
    * @methodName :
    * @date : 2023-04-21 오전 11:08
    * @author : 김재성
    * @Description: 복사하기 request dto
    **/
    @Data
    static class CopyRequest{

        private LocalDate startDate;        //시작날짜

        private LocalDate endDate;          //종료날짜

        private LocalDate targetDate;       //대상날짜
    }

    @Data
    static class MoveRequest{

        private LocalDate moveDate;        //시작날짜

        private LocalDate targetDate;       //대상날짜
    }

}
