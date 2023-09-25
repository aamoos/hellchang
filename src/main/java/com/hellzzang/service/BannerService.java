package com.hellzzang.service;

import com.hellzzang.dto.BannerFileDto;
import com.hellzzang.dto.QBannerFileDto;
import com.hellzzang.entity.Banner;
import com.hellzzang.repository.BannerRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import static com.hellzzang.entity.QBannerFile.bannerFile;

/**
 * packageName    : com.hellzzangAdmin.service
 * fileName       : BannerService
 * author         : 김재성
 * date           : 2023-05-16
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-05-16        김재성       최초 생성
 */

@Service
@RequiredArgsConstructor
@Transactional
public class BannerService {
    private final JPAQueryFactory jpaQueryFactory;
    private final BannerRepository bannerRepository;

    @Transactional(readOnly = true)
    public List<BannerFileDto> findBannerFileList(String bannerPath){

        Optional<Banner> optionalBanner = bannerRepository.findByBannerPathAndDelYn(bannerPath, "N");
        List<BannerFileDto> content = null;

        if(optionalBanner.isEmpty()){
            optionalBanner = bannerRepository.findByBannerPathAndDelYn("all", "N");
        }

        if(optionalBanner.isPresent()){
            Banner banner = optionalBanner.get();
            content = jpaQueryFactory
                    .select(new QBannerFileDto(
                            bannerFile.fileInfo.id
                            ,bannerFile.fileInfo
                    ))
                    .from(bannerFile)
                    .where(bannerFile.fileInfo.delYn.eq("N"))
                    .where(bannerFile.banner.id.eq(banner.getId()))
                    .orderBy(bannerFile.id.desc())
                    .fetch();
        }
        return content;
    }
}
