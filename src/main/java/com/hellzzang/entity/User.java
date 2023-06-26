package com.hellzzang.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

//UserService에서 설정
@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @JsonIgnore
    @Id
    @Column(name = "user_index")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //사용자 인덱스

    @Column(name = "login_id", length = 50, unique = true)
    private String userid; //로그인 아이디

    @Column(name = "password", length = 100)
    private String password; //비밀번호

    @Column(name = "username", length = 50)
    private String username; //이름

    @Column(name = "nickname", length = 50)
    private String nickname; //닉네임

    @Column(name = "address", length = 50)
    private String address; //주소

    @Column(name = "addressDetail", length = 50)
    private String addressDetail; //상세주소

    @Column(name = "phone", length = 50)
    private String phone; //핸드폰

    private String dorYn; //휴면여부

    private String delYn; //삭제여부
    
    private LocalDateTime joinDate; //가입날짜

    private String blockYn; //정지여부

    private LocalDateTime blockDate; //정지날짜

    private LocalDateTime lastLoginDate; //마지막 접속날짜

    @Column(nullable = true)
    private Long thumbnailIdx;          //사용자 thumbnail Idx

    @JsonIgnore
    @Column(name = "activated")
    private boolean activated;

    private String socialId;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "user_index", referencedColumnName = "user_index")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<Authority> authorities;
}
