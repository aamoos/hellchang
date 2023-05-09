package com.hellzzang.exception;

/**
 * packageName    : com.hellzzang.exception
 * fileName       : UserNotFoundException
 * author         : hj
 * date           : 2023-05-09
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-05-09        hj       최초 생성
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("Could not find user " + id);
    }
}