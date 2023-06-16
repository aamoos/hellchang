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
    public List<BannerFileDto> list(@RequestBody FindRequestDto findRequestDto, @RequestHeader(name="Authorization") String token) throws Exception {
        return bannerService.findBannerFileList(findRequestDto.bannerPath);
    }

    @Data
    static class FindRequestDto{

        @NotBlank(message = "배너 path는 필수항목입니다.")
        private String bannerPath;            //배너 path
    }

}
