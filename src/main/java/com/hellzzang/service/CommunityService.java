package com.hellzzang.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hellzzang.dto.*;
import com.hellzzang.entity.*;
import com.hellzzang.jwt.TokenProvider;
import com.hellzzang.repository.CommunityFileRepository;
import com.hellzzang.repository.CommunityRepository;
import com.hellzzang.repository.UserRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.hellzzang.entity.QCommunity.community;
import static com.hellzzang.entity.QGymWear.gymWear;

/**
 * packageName    : com.hellzzang.service
 * fileName       : CommunityService
 * author         : 김재성
 * date           : 2023-09-19
 * description    : 커뮤니티 controller
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-09-19        김재성       최초 생성
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final CommunityFileRepository communityFileRepository;
    private final FileService fileService;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final JPAQueryFactory jpaQueryFactory;

    public Page<CommunityDto> selectCommunityList(Pageable pageable, String searchVal){
        //admin 사용자 리스트 조회
        List<CommunityDto> content = getCommunityList(pageable, searchVal);

        //admin 사용자 total 조회
        Long count = getCount(searchVal);
        return new PageImpl<>(content, pageable, count);
    }

    private List<CommunityDto> getCommunityList(Pageable pageable, String searchVal) {

        List<CommunityDto> content = jpaQueryFactory
                .select(new QCommunityDto(
                         community.id
                        ,community.title
                        ,community.contents
                        ,community.createdDate
                        ,community.lastModifiedDate
                        ,community.thumbnailIdx
                        ,community.delYn
                        ,community.user
                ))
                .from(community)
                .where(community.delYn.eq("N"))
                .where(containsSearch(searchVal))
                .orderBy(community.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return content;
//        return null;
    }

    private Long getCount(String searchVal){
        Long count = jpaQueryFactory
                .select(community.count())
                .from(community)
                .where(community.delYn.eq("N"))
                .where(containsSearch(searchVal))
                .fetchOne();
        return count;
    }

    private BooleanExpression containsSearch(String searchVal){
        return searchVal != null && !searchVal.equals("") ? community.title.contains(searchVal) : null;
    }

    /**
    * @methodName : save
    * @date : 2023-09-19 오후 1:45
    * @author : 김재성
    * @Description: 커뮤니티 저장
    **/
    public Long save(CommunityDto communityDto, List<MultipartFile> communityFiles, HttpServletRequest request, String token){

        //작성한 사용자 조회
        User user = userRepository.findById(tokenProvider.getJwtTokenId(token)).orElseThrow(NullPointerException::new);
        communityDto.setUser(user);

        //게시글 저장
        Community community = communityDto.toEntity();
        Community saveCommunity = communityRepository.save(community);

        //멀티파일 업로드 (공통 파일 이력 테이블 insert)
         List<FileInfo> fileInfos = null;
        try {
            fileInfos = fileService.MultiUploadFile(request, communityFiles);
        } catch (IOException e) {
            throw new RuntimeException(e);
         }

        //커뮤니티 파일 첨부 테이블 insert
        int index = 0;
        for (FileInfo fileInfo : fileInfos) {
            FileInfo file = fileInfos.get(index);

            if(index == 0){
                //변경감지 첨부파일 첫번째 이미지 idx를 썸네일 idx로 지정
                saveCommunity.updateThumbnailIdx(file.getId());
            }

             CommunityFile communityFile = CommunityFile.builder()
                 .community(saveCommunity)
                 .fileInfo(fileInfo)
                 .build();

            communityFileRepository.save(communityFile);
            index++;
        }

        return saveCommunity.getId();
    }

}
