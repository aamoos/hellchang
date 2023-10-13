package com.hellzzang.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.*;
import java.time.LocalDateTime;

//UserService에서 설정
@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //사용자 인덱스

    private String userId; //로그인 아이디

    @JsonIgnore
    private String password; //비밀번호

    private String userName; //이름

    private String nickName; //닉네임

    private String address; //주소

    private String addressDetail; //상세주소

    private String phone; //핸드폰

    private String dorYn; //휴면여부

    private String delYn; //삭제여부

    private String blockYn; //정지여부

    private LocalDateTime blockDate; //정지날짜

    @LastModifiedDate
    private LocalDateTime lastLoginDate; //마지막 접속날짜

    private Long thumbnailIdx;          //사용자 thumbnail Idx

    @JsonIgnore
    @Column(name = "activated")
    private boolean activated;

    private String socialId;

    //권한 단일로 변경
    private String authority;

    @Builder
    public User(Long id, String userId, String password, String userName, String nickName, String address,
                String addressDetail, String phone, LocalDateTime lastLoginDate,Long thumbnailIdx){
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.userName = userName;
        this.nickName = nickName;
        this.address = address;
        this.addressDetail = addressDetail;
        this.phone = phone;
        this.dorYn = "N";
        this.delYn = "N";
        this.blockYn = "N";
        this.lastLoginDate = lastLoginDate;
        this.authority = "ROLE_USER";
        this.thumbnailIdx = thumbnailIdx;
        this.activated = true;
    }

    //비밀번호 변경
    public void changePassword(String password){
        this.password = password;
    }

    //소셜 아이디 설정
    public void changeSocialId(String socialId){
        this.socialId = socialId;
    }
}
