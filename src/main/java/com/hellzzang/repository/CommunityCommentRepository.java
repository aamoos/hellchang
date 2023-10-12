package com.hellzzang.repository;

import com.hellzzang.entity.CommunityComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommunityCommentRepository extends JpaRepository<CommunityComment, Long> {
    Optional<CommunityComment> findByParentId(Long id);
}
