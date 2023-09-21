package com.hellzzang.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hellzzang.common.ValidationErrorResponse;
import com.hellzzang.dto.CommunityDto;
import com.hellzzang.dto.GymWearDto;
import com.hellzzang.jwt.TokenProvider;
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
import java.io.IOException;
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

    @Data
    @NoArgsConstructor
    static class CommunityListDto{
        private String title;
        private int page;
        private int size;
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


}
