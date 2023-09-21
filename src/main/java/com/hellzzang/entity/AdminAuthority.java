package com.hellzzang.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

//UserService에서 설정
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminAuthority {

    @Id
    @Column(name = "authority_name", length = 50)
    private String authorityName;
}