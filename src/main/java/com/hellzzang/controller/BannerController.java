package com.hellzzang.controller;

import com.hellzzang.dto.BannerFileDto;
import com.hellzzang.service.BannerService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * packageName    : com.hellzzang.controller
 * fileName       : BannerController
 * author         : 김재성
 * date           : 2023-05-31
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-05-31        김재성       최초 생성
 */

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/banner")
public class BannerController {

    private final BannerService bannerService;

    @PostMapping("/list")
    public List<BannerFileDto> list(@RequestBody String bannerPath){
        return bannerService.findBannerFileList(bannerPath);
    }
}
