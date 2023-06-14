package com.hellzzang.repository;

import com.hellzzang.entity.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * packageName    : com.hellzzangAdmin.repository
 * fileName       : FileRepository
 * author         : 김재성
 * date           : 2023-05-11
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-05-11        김재성       최초 생성
 */
public interface FileRepository extends JpaRepository<FileInfo, Long> {

}
