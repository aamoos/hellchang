package com.hellzzang.repository;

import com.hellzzang.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * packageName    : com.hellzzang.repository
 * fileName       : CommentRepository
 * author         : hj
 * date           : 2023-06-13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-06-13        hj       최초 생성
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
