package com.hellchang.common;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * packageName    : com.hellchang.common
 * fileName       : Result
 * author         : 김재성
 * date           : 2023-04-19
 * description    : rest api에서 리스트 return 할때 쓰는 공통 dto
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-04-19        김재성       최초 생성
 */
@Data
@AllArgsConstructor
public class Result<T> {
    private int count;
    private T data; // 리스트의 값
}
