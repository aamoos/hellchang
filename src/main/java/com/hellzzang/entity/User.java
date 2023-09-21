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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity{

    @JsonIgnore
    @Id
    @Column(name = "user_index")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //사용자 인덱스

    private String userid; //로그인 아이디

    private String password; //비밀번호

    private String username; //이름

    private String nickname; //닉네임

    private String address; //주소

    private String addressDetail; //상세주소

    private String phone; //핸드폰

    private String dorYn; //휴면여부

    private String delYn; //삭제여부

    private String blockYn; //정지여부

    private LocalDateTime blockDate; //정지날짜

    private LocalDateTime lastLoginDate; //마지막 접속날짜

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

    @Builder
    public User(Long id, String userid, String password, String username, String nickname, String address,
                String addressDetail, String phone, LocalDateTime lastLoginDate, Set<Authority> authorities){
        this.id = id;
        this.userid = userid;
        this.password = password;
        this.username = username;
        this.nickname = nickname;
        this.address = address;
        this.addressDetail = addressDetail;
        this.phone = phone;
        this.lastLoginDate = lastLoginDate;
        this.authorities = authorities;
    }

}
