package com.hellzzang.controller;

import com.hellzzang.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * packageName    : com.hellzzang.controller
 * fileName       : BoardController
 * author         : hj
 * date           : 2023-06-13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-06-13        hj       최초 생성
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/board/list")
    public ResponseEntity<?> boardList(){
        return ResponseEntity.ok(boardService.boardList());
    }

    @GetMapping("/post/lists/{boardIdx}")
    public ResponseEntity<?> postLists(@PathVariable("boardIdx") Long boardIdx){
        return ResponseEntity.ok(boardService.postLists(boardIdx));
    }
}
