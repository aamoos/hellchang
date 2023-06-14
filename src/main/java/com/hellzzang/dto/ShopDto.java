package com.hellzzang.dto;

import com.hellzzang.entity.Shop;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * packageName    : com.hellzzang.dto
 * fileName       : ShopDto
 * author         : hj
 * date           : 2023-06-08
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-06-08        hj       최초 생성
 */
@Data
public class ShopDto {

    private Long id;
    private String menuName;
    private int depth;
    private Shop parent;
    private List<ShopDto> children;

    public ShopDto(final Shop shop) {
        this.id = shop.getId();
        this.menuName = shop.getMenuName();
        this.depth = shop.getDepth();
        this.parent = shop.getParent();
        this.children = shop.getChildren().stream().map(ShopDto::new).collect(Collectors.toList());
    }
}
