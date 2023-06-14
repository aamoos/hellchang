package com.hellzzang.repository;

import com.hellzzang.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * packageName    : com.hellzzang.repository
 * fileName       : ShopRepository
 * author         : hj
 * date           : 2023-06-08
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-06-08        hj       최초 생성
 */
public interface ShopRepository extends JpaRepository<Shop, Long> {
}
