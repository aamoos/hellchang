package com.hellzzang.controller;

import com.hellzzang.dto.ExerciseDto;
import com.hellzzang.dto.GymWearDto;
import com.hellzzang.dto.GymWearFileDto;
import com.hellzzang.entity.GymWear;
import com.hellzzang.entity.GymWearOption;
import com.hellzzang.service.GymWearService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * packageName    : com.hellzzang.controller
 * fileName       : GymwearController
 * author         : 김재성
 * date           : 2023-06-12
 * description    : 짐웨어 controller
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-06-12        김재성       최초 생성
 */

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/gymWear")
public class GymWearController {

    private final GymWearService gymWearService;

    @PostMapping("/list")
    public Page<GymWearDto> find(@RequestBody GymWearListDto gymWearListDto, Pageable pageable, @RequestHeader(name="Authorization") String token) throws Exception {
        pageable = PageRequest.of(gymWearListDto.getPage(), gymWearListDto.getSize());
        return gymWearService.selectGymWearList(pageable, gymWearListDto.getTitle());
    }

    @PostMapping("/detail")
    public GymWearDetailResponse getGymWearDetail(@RequestBody GymWearDetailRequest gymWearDetailRequest, @RequestHeader(name="Authorization") String token){
        GymWear gymWear = gymWearService.find(gymWearDetailRequest.getId());

        return GymWearDetailResponse.builder()
                .entity(gymWear)
                .build();
    }

    @Data
    @NoArgsConstructor
    static class GymWearListDto{
        private String title;
        private int page;
        private int size;
    }

    @Data
    @NoArgsConstructor
    static class GymWearDetailRequest{
        private Long id;
    }

    @Data
    @NoArgsConstructor
    static class GymWearDetailResponse{
        private Long id;

        @NotBlank(message = "제목을 입력해주세요.")
        private String title;

        @Lob
        @Column(name = "text_area", columnDefinition = "CLOB")
        @NotBlank(message = "내용을 입력해주세요.")
        private String contents;

        private Long thumbnailIdx;

        @NotNull(message = "가격은 0원 보다 크게 작성해야 합니다.")
        @Min(value = 1, message = "가격은 0원 보다 크게 작성해야 합니다.")
        private Long price;     //가격

        private List<GymWearOption> gymWearOptions = new ArrayList<>();

        @Builder
        public GymWearDetailResponse(GymWear entity){
            this.id = entity.getId();
            this.title = entity.getTitle();
            this.contents = entity.getContents();
            this.thumbnailIdx = entity.getThumbnailIdx();
            this.price = entity.getPrice();

            for (GymWearOption gymWearOption : entity.getGymWearOptions()) {
                GymWearOption go = GymWearOption.builder()
                        .id(gymWearOption.getId())
                        .optionName(gymWearOption.getOptionName())
                        .build();
                gymWearOptions.add(go);
            }

        }
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SaveGymWear{

        private Long id;

        @NotBlank(message = "제목을 입력해주세요.")
        private String title;

        @Lob
        @Column(name = "text_area", columnDefinition = "CLOB")
        @NotBlank(message = "내용을 입력해주세요.")
        private String contents;

        private Long thumbnailIdx;

        private List<Long> contentFileIdx;

        @NotNull(message = "가격은 0원 보다 크게 작성해야 합니다.")
        @Min(value = 1, message = "가격은 0원 보다 크게 작성해야 합니다.")
        private Long price;     //가격

        @Builder
        public SaveGymWear(Long id, String title, String contents, Long thumbnailIdx, List<Long> contentFileIdx, Long price){
            this.id = id;
            this.title = title;
            this.contents = contents;
            this.thumbnailIdx = thumbnailIdx;
            this.contentFileIdx = contentFileIdx;
            this.price = price;
        }
    }

}
