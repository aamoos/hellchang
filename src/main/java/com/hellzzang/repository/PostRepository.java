package com.hellzzang.repository;

import com.hellzzang.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * packageName    : com.hellzzang.repository
 * fileName       : PostRepository
 * author         : hj
 * date           : 2023-06-13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-06-13        hj       최초 생성
 */
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByBoardId(Long id);

}
