package com.hellzzang.service;

import com.hellzzang.entity.User;
import com.hellzzang.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //토큰 발급 시 호출되는 메서드 from /Authenticate
    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String userId) {
        //유저 정보와 권한 정보를 리턴
        return userRepository.findOneWithAuthoritiesByuserId(userId)
                .map(users -> createUser(userId, users))
                .orElseThrow(() -> new UsernameNotFoundException(userId + " -> 데이터베이스에서 찾을 수 없습니다."));
    }


    //해당하는 User의 데이터가 존재한다면 UserDetails 객체로 만들어서 리턴
    private org.springframework.security.core.userdetails.User createUser(String userId, User user) {
        if (!user.isActivated()) {
            throw new RuntimeException(userId + " -> 활성화되어 있지 않습니다.");
        }

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        return new org.springframework.security.core.userdetails.User(user.getUserId(),
                user.getPassword(),
                authorities);
    }
}