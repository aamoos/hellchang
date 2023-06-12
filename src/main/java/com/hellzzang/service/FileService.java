package com.hellzzang.service;

import com.hellzzang.entity.FileInfo;
import com.hellzzang.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

/**
 * packageName    : com.hellzzangAdmin.service
 * fileName       : FileService
 * author         : 김재성
 * date           : 2023-05-11
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-05-11        김재성       최초 생성
 */
@Slf4j
@Service("fileService")
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    /**
    * @methodName : getThumbnail
    * @date : 2023-05-11 오후 5:17
    * @author : 김재성
    * @Description: 넘어온 파일 인덱스로 썸네일 보여주기
    **/
    public ResponseEntity<byte[]> getThumbnail(Long fileIdx) throws Exception {
        ResponseEntity<byte[]> result = null;

        Optional<FileInfo> optionalFile = fileRepository.findById(fileIdx);

        if(optionalFile.isPresent()){

            FileInfo fileInfo = optionalFile.get();

            String logiPath = fileInfo.getUploadDir();
            String logiNm = fileInfo.getSavedFileName();

            File file = new File(logiPath+"/"+logiNm);

            try {
                HttpHeaders headers=new HttpHeaders();
                headers.add("Content-Type", Files.probeContentType(file.toPath()));
                result=new ResponseEntity<>(FileCopyUtils.copyToByteArray(file),headers, HttpStatus.OK );

            }catch (IOException e) {
                e.printStackTrace();
            }

        }else{
            throw new Exception("해당 파일정보가 없습니다.");
        }

        return result;
    }

}
