package com.hellzzang.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @package : com.hellzzangAdmin.entity
 * @name : AdminUsers.java
 * @date : 2023-06-17 오전 12:21
 * @author : 김재성
 * @Description: 괸리자 entity
 **/
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminUsers extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //사용자 인덱스

    private String userId; //로그인 아이디

    private String password; //비밀번호

    private String userName; //이름

    private String nickName;

    private String delYn;   //삭제여부

    @JsonIgnore
    @Column(name = "activated")
    private boolean activated;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "admin_user_authority",
            joinColumns = {@JoinColumn(name = "id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<AdminAuthority> authorities;

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities.stream()
                .map(adminAuthority -> new SimpleGrantedAuthority(adminAuthority.getAuthorityName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return this.userId;
    }

    /**
     * @methodName : delete
     * @date : 2023-05-15 오전 10:50
     * @author : 김재성
     * @Description: 관리자 삭제
     **/
    public AdminUsers delete(){
        this.delYn = "Y";
        return this;
    }

}
