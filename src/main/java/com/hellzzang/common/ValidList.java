package com.hellzzang.common;

import lombok.Data;
import lombok.experimental.Delegate;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


/**
 * packageName    : com.hellchang.common
 * fileName       : ValidList
 * author         : 김재성
 * date           : 2023-04-18
 * description    : 리스트<dto> 형식의 request를 validation 검증할때 사용하는 class
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-04-18        김재성       최초 생성
 */

@Data
public class ValidList<E> implements List<E> {
    @Valid
    @Delegate
    private List<E> list = new ArrayList<>();
}
