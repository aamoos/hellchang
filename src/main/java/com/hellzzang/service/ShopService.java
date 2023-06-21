package com.hellzzang.service;

import com.hellzzang.dto.ShopDto;
import com.hellzzang.entity.QShop;
import com.hellzzang.entity.Shop;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * packageName    : com.hellzzang.service
 * fileName       : ShoppService
 * author         : hj
 * date           : 2023-06-08
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-06-08        hj       최초 생성
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ShopService {

    private final JPAQueryFactory jpaQueryFactory;

    /**
    * @methodName : menuList
    * @date : 2023-06-16 오전 9:07
    * @author : hj
    * @Description: 짐웨어&장비 사이드 메뉴 리스트 출력
    **/
    public List<ShopDto> menuList(){
        QShop parent = QShop.shop;

        List<Shop> result = jpaQueryFactory
                .select(parent)
                .from(parent)
                .where(parent.parent.isNull())
                .orderBy(parent.id.asc())
                .fetch();

        return result.stream().map(ShopDto::new).collect(Collectors.toList());
    }
}
