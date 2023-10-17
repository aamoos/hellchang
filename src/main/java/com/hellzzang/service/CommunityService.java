package com.hellzzang.service;

import com.hellzzang.dto.*;
import com.hellzzang.entity.*;
import com.hellzzang.jwt.TokenProvider;
import com.hellzzang.repository.CommunityCommentRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static com.hellzzang.entity.QCommunity.community;
import static com.hellzzang.entity.QCommunityFile.communityFile;
import static com.hellzzang.entity.QCommunityComment.communityComment;

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
    private final CommunityCommentRepository commentRepository;

    /**
    * @methodName : selectCommunityList
    * @date : 2023-09-25 오후 3:08
    * @author : 김재성
    * @Description: 커뮤니티 리스트 조회하기
    **/
    public Page<CommunityDto> selectCommunityList(Pageable pageable, String searchVal){
        //admin 사용자 리스트 조회
        List<CommunityDto> content = getCommunityList(pageable, searchVal);

        //admin 사용자 total 조회
        Long count = getCount(searchVal);
        return new PageImpl<>(content, pageable, count);
    }

    // 게시글의 댓글 전체 가져오기
    public Page<CommunityComment> selectCommunityCommentList(Pageable pageable, Long id){

        List<CommunityComment> content = getCommunityCommentList(pageable, id);

        Long count = getCommunityCommentCount(id);
        return new PageImpl<>(content, pageable, count);
    }

    private List<CommunityComment> getCommunityCommentList(Pageable pageable, Long id) {

//        List<CommunityCommentDto> content = jpaQueryFactory
//                .select(new QCommunityCommentDto(
//                         communityComment.id
//                        ,communityComment.user
//                        ,communityComment.community
//                        ,communityComment.content
//                        ,communityComment.parent
//                        ,communityComment.children
//                ))
//                .from(communityComment)
//                .where(communityComment.community.id.eq(id))
//                .orderBy(communityComment.id.desc())
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//
//        return content;

        List<CommunityComment> contents = jpaQueryFactory.selectFrom(communityComment)
                .leftJoin(communityComment.parent)
//                .fetchJoin()
//                .leftJoin(communityComment.user).fetchJoin()
//                .leftJoin(communityComment.community).fetchJoin()
                .where(communityComment.community.id.eq(id), communityComment.parent.id.isNull())
                .orderBy(communityComment.parent.id.asc().nullsFirst(), communityComment.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return contents;

//        return jpaQueryFactory.selectFrom(communityComment)
//                .leftJoin(communityComment.parent)
//                .fetchJoin()
//                .where(communityComment.community.id.eq(id))
//                .orderBy(communityComment.parent.id.asc().nullsFirst(), communityComment.id.asc())
//                .fetch();
    }

    private Long getCommunityCommentCount(Long id){
//        Long count = jpaQueryFactory
//                .select(communityComment.count())
//                .from(communityComment)
//                .where(communityComment.community.id.eq(id))
//                .fetchOne();
//        return count;

        return jpaQueryFactory.select(communityComment.count())
                .from(communityComment)
                .leftJoin(communityComment.parent)
//                .fetchJoin()
//                .leftJoin(communityComment.user).fetchJoin()
//                .leftJoin(communityComment.community).fetchJoin()
//                .fetchJoin()
                .where(communityComment.community.id.eq(id), communityComment.parent.id.isNull())
                .fetchOne();
    }

    /**
    * @methodName : selectCommunityDetail
    * @date : 2023-09-25 오후 3:08
    * @author : 김재성
    * @Description: 커뮤니티 상세 조회하기
    **/
    public CommunityDto selectCommunityDetail(Long id){
        CommunityDto content = getCommunityDetail(id);
        return content;
    }

    public List<CommunityFileDto> selectCommunityDetailFile(Long id){
        List<CommunityFileDto> content = getCommunityDetailFile(id);
        return content;
    }

    /**
    * @methodName : getCommunityList
    * @date : 2023-09-25 오후 3:08
    * @author : 김재성
    * @Description: 커뮤니티 페이징 리스트
    **/
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
    }

    private CommunityDto getCommunityDetail(Long id){

        CommunityDto content = jpaQueryFactory
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
                .where(community.id.eq(id))
                .fetchOne();

        return content;
    }

    private List<CommunityFileDto> getCommunityDetailFile(Long id){

        List<CommunityFileDto> content = jpaQueryFactory
                .select(new QCommunityFileDto(
                         communityFile.fileInfo.id
                ))
                .from(communityFile)
                .where(communityFile.community.id.eq(id))
                .fetch();

        return content;
    }

    /**
    * @methodName : getCount
    * @date : 2023-09-25 오후 3:08
    * @author : 김재성
    * @Description: 커뮤니티 total count
    **/
    private Long getCount(String searchVal){
        Long count = jpaQueryFactory
                .select(community.count())
                .from(community)
                .where(community.delYn.eq("N"))
                .where(containsSearch(searchVal))
                .fetchOne();
        return count;
    }

    /**
    * @package : com.hellzzang.service
    * @name : CommunityService.java
    * @date : 2023-09-25 오후 3:07
    * @author : 김재성
    * @Description: 커뮤니티 검색 조회
    **/
    private BooleanExpression containsSearch(String searchVal){
        return searchVal != null && !searchVal.equals("") ? community.title.contains(searchVal) : null;
    }

    /**
    * @methodName : save
    * @date : 2023-09-25 오후 3:07
    * @author : 김재성
    * @Description: 커뮤니티 저장
    **/   
    public Long save(CommunityDto communityDto, List<MultipartFile> communityFiles, HttpServletRequest request, String token){

        //작성한 사용자 조회
        User user = userRepository.findById(tokenProvider.getJwtTokenId(token)).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
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

    public Long saveComment(CommunityCommentDto requestDto, String token) {

        //작성한 사용자 조회
        User user = userRepository.findById(tokenProvider.getJwtTokenId(token)).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Optional<Community> optionalCommunity = communityRepository.findById(requestDto.getCommunityId());

        optionalCommunity.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글 id 입니다."));

        Community community = optionalCommunity.get();

        CommunityComment parent = null;

        // 자식댓글인 경우
        if(requestDto.getParentId() != null){
            Optional<CommunityComment> optionalParent = commentRepository.findById(requestDto.getParentId());
            optionalParent.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글 id 입니다."));

            parent = optionalParent.get();

            // 부모댓글의 게시글 번호와 자식댓글의 게시글 번호 같은지 체크하기
            if(parent.getCommunity().getId() != requestDto.getCommunityId()){
                new IllegalArgumentException("부모댓글과 자식댓글의 게시글 번호가 일치하지 않습니다.");
            }
        }

        CommunityComment comment = CommunityComment.builder()
                .user(user)
                .community(community)
                .content(requestDto.getContent())
                .build();

        //visible 처음 false 처리
        comment.defaultElementVisible();

        if(null != parent){
            comment.updateParent(parent);
        }

        //부모일때
        commentRepository.save(comment);

        return comment.getId();
    }
}
