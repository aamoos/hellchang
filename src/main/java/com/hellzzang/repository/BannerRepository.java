package com.hellzzang.repository;

import com.hellzzang.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * packageName    : com.hellzzangAdmin.repository
 * fileName       : BannerRepository
 * author         : 김재성
 * date           : 2023-05-16
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-05-16        김재성       최초 생성
 */
public interface BannerRepository extends JpaRepository<Banner, Long> {

     Optional<Banner> findByBannerPathAndDelYn(String bannerPath, String delYn);

}
