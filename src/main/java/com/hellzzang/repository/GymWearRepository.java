package com.hellzzang.repository;

import com.hellzzang.entity.FileInfo;
import com.hellzzang.entity.GymWear;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * packageName    : com.hellzzang.repository
 * fileName       : GymWearRepository
 * author         : 김재성
 * date           : 2023-06-12
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-06-12        김재성       최초 생성
 */
public interface GymWearRepository extends JpaRepository<GymWear, Long> {

    Page<GymWear> findByTitleContainingOrderByIdDesc(String title, Pageable pageable);

}
