package com.hellzzang.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * packageName    : com.hellchang.entity
 * fileName       : Exercise
 * author         : 김재성
 * date           : 2023-04-11
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-04-11        김재성       최초 생성
 */

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                        //인덱스 idx

    private Long userId;                    //사용자 idx

    private String exerciseName;            //운동명

    private int setCount;                   //세트

    private int kilogram;                   //kg

    private int reps;                       //회

    private String delYn;                   //삭제여부

    private String completeYn;              //완료여부

    @Column
    private LocalDate exerciseDate;            //운동날짜

    @Builder
    public Exercise(Long userId,String exerciseName, int setCount, int kilogram, int reps, String delYn, String completeYn, LocalDate exerciseDate){
        this.userId = userId;
        this.exerciseName = exerciseName;
        this.setCount = setCount;
        this.kilogram = kilogram;
        this.reps = reps;
        this.delYn = delYn;
        this.completeYn = completeYn;
        this.exerciseDate = exerciseDate;
    }

    /**
    * @methodName : updateCompleteYn
    * @date : 2023-04-19 오후 4:40
    * @author : 김재성
    * @Description: 완료여부 업데이트
    **/
    public void updateCompleteYn(String completeYn){
        this.completeYn = completeYn;
    }

    /**
    * @methodName : updateDelYn
    * @date : 2023-04-21 오후 1:34
    * @author : 김재성
    * @Description: 삭제여부 업데이트
    **/
    public void updateDelYn(String delYn){
        this.delYn = delYn;
    }

    /**
    * @methodName : updateExerciseDate
    * @date : 2023-04-24 오전 11:02
    * @author : 김재성
    * @Description: 운동날짜 업데이트
    **/
    public void updateExerciseDate(LocalDate exerciseDate){
        this.exerciseDate = exerciseDate;
    }

}
