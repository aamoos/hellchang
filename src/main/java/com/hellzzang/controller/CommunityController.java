package com.hellzzang.controller;

import com.hellzzang.common.ValidationErrorResponse;
import com.hellzzang.dto.CommunityCommentDto;
import com.hellzzang.dto.CommunityDto;
import com.hellzzang.dto.CommunityFileDto;
import com.hellzzang.entity.CommunityComment;
import com.hellzzang.service.CommunityService;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * packageName    : com.hellzzang.controller
 * fileName       : CommunityController
 * author         : 김재성
 * date           : 2023-09-18
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-09-18        김재성       최초 생성
 */

@RestController
@RequestMapping("/community")
@RequiredArgsConstructor
@Slf4j
public class CommunityController {

    private final CommunityService communityService;

    @PostMapping("/list")
    public Page<CommunityDto> find(@RequestBody CommunityListDto communityListDto, Pageable pageable, @RequestHeader(name="Authorization") String token) throws Exception {
        pageable = PageRequest.of(communityListDto.getPage(), communityListDto.getSize());
        return communityService.selectCommunityList(pageable, communityListDto.getTitle());
    }

    @PostMapping("/comment/list")
    public Page<CommunityComment> commentList(@RequestBody CommunityCommentDto communityCommentDto, Pageable pageable) throws Exception {
        pageable = PageRequest.of(communityCommentDto.getPage(), communityCommentDto.getSize());
        return communityService.selectCommunityCommentList(pageable, communityCommentDto.getCommunityId());
    }

    @PostMapping("/detail")
    public CommunityDto communityDetail(@RequestBody CommunityDetailDto communityDetailDto) {
        return communityService.selectCommunityDetail(communityDetailDto.getId());
    }

    @PostMapping("/detailFile")
    public List<CommunityFileDto> communityDetailFile(@RequestBody CommunityDetailDto communityDetailDto) {
        return communityService.selectCommunityDetailFile(communityDetailDto.getId());
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@Valid CommunityDto communityDto, BindingResult result
            , @RequestPart(value = "communityFiles", required = false) List<MultipartFile> communityFiles
            , HttpServletRequest request
            , @RequestHeader(name="Authorization") String token){
        log.info("bindingResult ={}", result);

        if (result.hasErrors()) {
            Map<String, List<String>> fieldErrors = new HashMap<>();
            result.getFieldErrors().forEach(fieldError -> {
                String field = fieldError.getField();
                String message = fieldError.getDefaultMessage();
                fieldErrors.computeIfAbsent(field, key -> new ArrayList<>()).add(message);
            });

            return ResponseEntity.badRequest().body(new ValidationErrorResponse(fieldErrors));
        }

        return ResponseEntity.ok(communityService.save(communityDto, communityFiles, request, token));
    }

    @PostMapping("/comment/save")
    public ResponseEntity<?> commentSave(@Valid @RequestBody CommunityCommentDto communityCommentRequestDto, BindingResult result
            , HttpServletRequest request
            , @RequestHeader(name="Authorization") String token){
        log.info("bindingResult ={}", result);

        if (result.hasErrors()) {
            Map<String, List<String>> fieldErrors = new HashMap<>();
            result.getFieldErrors().forEach(fieldError -> {
                String field = fieldError.getField();
                String message = fieldError.getDefaultMessage();
                fieldErrors.computeIfAbsent(field, key -> new ArrayList<>()).add(message);
            });

            return ResponseEntity.badRequest().body(new ValidationErrorResponse(fieldErrors));
        }

        return ResponseEntity.ok(communityService.saveComment(communityCommentRequestDto, token));
    }

    @Data
    @NoArgsConstructor
    static class CommunityListDto{
        private String title;
        private int page;
        private int size;
    }

    @Data
    @NoArgsConstructor
    static class CommunityDetailDto{
        private Long id;
    }


}
