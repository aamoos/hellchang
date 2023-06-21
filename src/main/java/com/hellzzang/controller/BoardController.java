package com.hellzzang.controller;

import com.hellzzang.service.BoardService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

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

    @GetMapping("/detailContent/{postIdx}")
    public ResponseEntity<?> detailContent(@PathVariable("postIdx") Long postIdx){
        return ResponseEntity.ok(boardService.detailContent(postIdx));
    }

    @GetMapping("/commentList/{postIdx}")
    public ResponseEntity<?> commentList(@PathVariable("postIdx") Long postIdx){
        return ResponseEntity.ok(boardService.commentList(postIdx));
    }

    @PostMapping("/writeReply")
    @ResponseBody
    public void writeReply(@Valid @RequestBody writeReplyDto request){
        boardService.writeReply(request.getPostId(), request.getUserId(), request.getParentId(), request.getComment());
//        return ResponseEntity.ok((boardService.writeComment(request.getPostId(), request.getUserId(), request.getParentId(), request.getComment())));
    }

    @Data
    static class writeReplyDto{
        @NotNull
        private Long postId;

        @NotNull
        private Long userId;

        private Long parentId;

        @NotBlank
        private String comment;

        private LocalDateTime commentDate;
    }
}
