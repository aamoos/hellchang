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
    }
    @PostMapping("/replyInfo")
    @ResponseBody
    public ResponseEntity<?> replyInfo(@RequestBody replyInfoDto request){
        return ResponseEntity.ok(boardService.replyInfo(request.getId()));
    }
    @PostMapping("/updateReply")
    @ResponseBody
    public void replyUpdate(@RequestBody replyUpdateDto request){
        boardService.replyUpdate(request.getId(), request.getComment());
    }
    @PostMapping("/writePost")
    @ResponseBody
    public void writePost(@RequestBody writePostDto request){
        boardService.writePost(request.getBoardId(), request.getUserId(), request.getTitle(), request.getContent(), request.getLikes());
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

    @Data
    static class replyInfoDto{
        @NotNull
        private Long id;
    }

    @Data
    static class replyUpdateDto{
        @NotNull
        private Long id;

        private String comment;
    }

    @Data
    static class writePostDto{
        private Long boardId;
        private Long userId;
        private String title;
        private String content;
        private int likes;
    }
}
