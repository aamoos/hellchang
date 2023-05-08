package com.hellzzang.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
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

    @Column(name = "userid", length = 50, unique = true)
    private String userid; //로그인 아이디

    @Column(name = "password", length = 100)
    private String password; //비밀번호

    @Column(name = "username", length = 50)
    private String username; //이름

    @Column(name = "nickname", length = 50)
    private String nickname; //닉네임

    @Column(name = "address", length = 50)
    private String address; //주소

    @Column(name = "phone", length = 50)
    private String phone; //핸드폰

    @JsonIgnore
    @Column(name = "activated")
    private boolean activated;

    @ManyToMany
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "user_index", referencedColumnName = "user_index")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<Authority> authorities;
}
