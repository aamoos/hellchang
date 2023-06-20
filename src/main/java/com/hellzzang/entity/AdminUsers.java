package com.hellzzang.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class AdminUsers implements UserDetails {

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

    @Column(name = "nickname")
    private String nickname;

    @CreatedDate
    private String createdDate;

    @LastModifiedDate
    private String modifiedDate;

    @PrePersist
    public void onPrePersist(){
        this.createdDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.modifiedDate = this.createdDate;
    }

    @PreUpdate
    public void onPreUpdate(){
        this.modifiedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private String delYn;   //삭제여부

    @JsonIgnore
    @Column(name = "activated")
    private boolean activated;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "admin_user_authority",
            joinColumns = {@JoinColumn(name = "user_index", referencedColumnName = "user_index")},
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
