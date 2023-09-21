package com.hellzzang.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * packageName    : com.hellzzangAdmin.entity
 * fileName       : GymWearOption
 * author         : 김재성
 * date           : 2023-06-14
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-06-14        김재성       최초 생성
 */

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GymWearOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String optionName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gymWear_id")
    private GymWear gymWear;

    @Builder
    public GymWearOption(Long id, String optionName, GymWear gymWear) {
        this.id = id;
        this.optionName = optionName;
        this.gymWear = gymWear;
    }
}
