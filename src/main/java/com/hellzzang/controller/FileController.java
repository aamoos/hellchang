package com.hellzzang.controller;

import com.hellzzang.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * packageName    : com.hellzzang.controller
 * fileName       : FileController
 * author         : 김재성
 * date           : 2023-05-31
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-05-31        김재성       최초 생성
 */

@Controller
@Slf4j
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @ResponseBody
    @GetMapping("/thumbnail/{fileIdx}")
    public ResponseEntity<byte[]> getFile(@PathVariable("fileIdx") Long fileIdx) throws Exception {
        return fileService.getThumbnail(fileIdx);
    }

}
