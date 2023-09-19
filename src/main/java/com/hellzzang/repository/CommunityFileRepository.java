package com.hellzzang.repository;

import com.hellzzang.entity.Community;
import com.hellzzang.entity.CommunityFile;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * packageName    : com.hellzzang.repository
 * fileName       : CommunityFileRepository
 * author         : 김재성
 * date           : 2023-09-19
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-09-19        김재성       최초 생성
 */
public interface CommunityFileRepository extends JpaRepository<CommunityFile, Long> {
}
