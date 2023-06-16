package com.hellzzang.service;

import com.hellzzang.dto.BannerFileDto;
import com.hellzzang.dto.QBannerFileDto;
import com.hellzzang.entity.Banner;
import com.hellzzang.repository.BannerRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

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
public class BannerService {
    private final JPAQueryFactory jpaQueryFactory;
    private final BannerRepository bannerRepository;

    public List<BannerFileDto> findBannerFileList(String bannerPath){

        Banner banner = bannerRepository.findByBannerPathAndDelYn(bannerPath, "N");

        //해당 등록된 path가 없으면 all로 등록된거 있는지 체크
        if(banner == null){
            banner = bannerRepository.findByBannerPathAndDelYn("all", "N");;
        }

        List<BannerFileDto> content = null;

        if(banner != null){
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
