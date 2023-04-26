package com.hellchang.controller;

import com.hellchang.common.Result;
import com.hellchang.common.ValidList;
import com.hellchang.entity.Exercise;
import com.hellchang.jwt.TokenProvider;
import com.hellchang.repository.ExerciseRepository;
import com.hellchang.service.ExerciseService;
import io.jsonwebtoken.Header;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Base64;
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
    @PostMapping("/exercise/list")
    public Result list(@RequestBody FindRequestDto findRequestDto, @RequestHeader(name="Authorization") String token) throws Exception {

        Map<String, Object> payloadMap = tokenProvider.getJwtTokenPayload(token);
        String id = String.valueOf(payloadMap.get("sub"));

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
    @PostMapping("/exercise/copy")
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
    @PostMapping("/exercise/move")
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
        exerciseService.updateDelYn(request.getExerciseDate(), request.getDelYn());

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

        @NotNull(message = "삭제여부는 필수값입니다.")
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

        @NotNull(message = "시작날짜는 필수값입니다.")
        private int startDate;        //시작날짜

        @NotNull(message = "종료날짜는 필수값입니다.")
        private int endDate;          //종료날짜

        @NotNull(message = "대상날짜는 필수값입니다.")
        private int targetDate;       //대상날짜
    }

    @Data
    static class MoveRequest{

        @NotNull(message = "이동할날짜는 필수값입니다.")
        private int moveDate;        //시작날짜

        @NotNull(message = "대상날짜는 필수값입니다.")
        private int targetDate;       //대상날짜
    }

}
