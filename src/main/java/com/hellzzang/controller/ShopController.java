package com.hellzzang.controller;

import com.hellzzang.dto.ShopDto;
import com.hellzzang.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * packageName    : com.hellzzang.controller
 * fileName       : MenuController
 * author         : hj
 * date           : 2023-06-08
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-06-08        hj       최초 생성
 */
@Controller
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    @PostMapping("/shopMenuList")
    public ResponseEntity<List<ShopDto>> menuList(){
        List<ShopDto> result = shopService.menuList();
        System.out.println(result.size());
        System.out.println(result);

        //하위 메뉴만 출력하기
//        List<ShopDto> frontResult = new ArrayList<>();
//
//        for(int i=0 ; i<result.size(); i++){
//            for(int j = 0; j<result.get(i).getChildren().size(); j++){
//                frontResult.add(i, result.get(i).getChildren().get(j));
//            }
//        }
//
//        Collections.sort(frontResult, new Comparator<ShopDto>() {
//            @Override
//            public int compare(ShopDto dto1, ShopDto dto2) {
//                return dto1.getId().compareTo(dto2.getId());
//            }
//        });
//
//        System.out.println(frontResult);

        return ResponseEntity.ok(result);
    }
}
