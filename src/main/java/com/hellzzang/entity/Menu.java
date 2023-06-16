package com.hellzzang.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * packageName    : com.hellzzangAdmin.entity
 * fileName       : Menu
 * author         : hj
 * date           : 2023-05-18
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-05-18        hj       최초 생성
 */

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Menu parent;

    private String name;

    private int depth;

    @OneToMany(mappedBy = "parent")
    private List<Menu> children = new ArrayList<>();

//    @OneToMany
//    @JoinColumn(name = "child")
//    private List<Board> board = new ArrayList<>();
}
