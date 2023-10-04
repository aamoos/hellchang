package com.hellzzang.service;

import com.hellzzang.dto.BannerFileDto;
import com.hellzzang.dto.QBannerFileDto;
import com.hellzzang.repository.BannerRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.hellzzang.entity.QBannerFile.bannerFile;

/**
 * packageName    : com.hellzzangAdmin.service
 * fileName       : BannerService
 * author         : 김재성
 * date           : 2023-05-16
 * description    : 배너 조회 서비스
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

    @Transactional(readOnly = true)
    public List<BannerFileDto> findBannerFileList(String bannerPath){

        return jpaQueryFactory
                .select(new QBannerFileDto(
                         bannerFile.fileInfo.id
                        ,bannerFile.fileInfo
                ))
                .from(bannerFile)
                .where(bannerFile.banner.bannerPath.in(bannerPath, "all"), bannerFile.banner.delYn.eq("N"))
                .orderBy(bannerFile.id.desc())
                .fetch();
    }

}
