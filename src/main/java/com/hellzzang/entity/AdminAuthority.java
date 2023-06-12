package com.hellzzang.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

//UserService에서 설정
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminAuthority {

    @Id
    @Column(name = "authority_name", length = 50)
    private String authorityName;
}