package com.hellchang.common;

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
 * description    :
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
