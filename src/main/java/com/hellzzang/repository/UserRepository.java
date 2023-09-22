package com.hellzzang.repository;

import com.hellzzang.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


/**
* @package : com.example.jwt.repository
* @name : UserRepository.java
* @date : 2023-04-19 오후 5:24
* @author : hj
* @Description: User Entity에 매핑을 위해 생성
 *              UserService에서 호출
**/

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userId);
    User findBySocialId(String id);
}