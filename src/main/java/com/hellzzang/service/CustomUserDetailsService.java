package com.hellzzang.service;

import com.hellzzang.entity.User;
import com.hellzzang.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;


//@Component("userDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    //사용자 정보 조회
    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String userId) {

        Optional<User> optionalUser = userRepository.findByUserId(userId);

        if(optionalUser.isPresent()){
            return (UserDetails) optionalUser.get();
        }else{
            throw new UsernameNotFoundException(userId + " -> 데이터베이스에서 찾을 수 없습니다.");
        }
    }
}