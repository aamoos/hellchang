package com.hellzzang.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hellzzang.dto.CommunityDto;
import com.hellzzang.dto.UserDto;
import com.hellzzang.entity.Community;
import com.hellzzang.entity.CommunityFile;
import com.hellzzang.entity.FileInfo;
import com.hellzzang.entity.User;
import com.hellzzang.jwt.TokenProvider;
import com.hellzzang.repository.CommunityFileRepository;
import com.hellzzang.repository.CommunityRepository;
import com.hellzzang.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

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
