package com.hellchang.repository;

import com.hellchang.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

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
    
}
