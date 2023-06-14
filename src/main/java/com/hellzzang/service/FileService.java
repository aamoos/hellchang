package com.hellzzang.service;

import com.hellzzang.dto.FileDto;
import com.hellzzang.entity.FileInfo;
import com.hellzzang.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

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

    @Value("${file.upload-path}")
    private String uploadFilePath;

    /** 단일 파일업로드 */
    public FileInfo uploadFile(HttpServletRequest request, MultipartFile multipartFile) throws Exception{

        FileDto fileDto = new FileDto();
        FileInfo fileInfo = null;

        String _filePath = String.valueOf(request.getParameter("filePath")).equals("null") ? uploadFilePath : uploadFilePath+String.valueOf(request.getParameter("filePath")+"/");


        try {
            if(multipartFile != null){

                // 파일이 있을때 탄다.
                if(multipartFile.getSize() > 0 && !multipartFile.getOriginalFilename().equals("")) {
                    String originalFileName = multipartFile.getOriginalFilename();    //오리지날 파일명
                    String extension = originalFileName.substring(originalFileName.lastIndexOf("."));    //파일 확장자
                    String savedFileName = UUID.randomUUID() + extension;    //저장될 파일 명

                    File targetFile = new File(_filePath + File.separator + savedFileName);
                    fileDto = FileDto.builder()
                            .originFileName(originalFileName)
                            .savedFileName(savedFileName)
                            .uploadDir(_filePath)
                            .extension(extension)
                            .size(multipartFile.getSize())
                            .contentType(multipartFile.getContentType())
                            .delYn("N")
                            .build();

                    //마지막 시퀀스 조회
                    fileInfo = fileRepository.save(fileDto.toEntity());
                    //썸네일 url 설정
                    fileInfo.updateUrl("/thumbnail/"+fileInfo.getId());

                    try {
                        InputStream fileStream = multipartFile.getInputStream();
                        FileUtils.copyInputStreamToFile(fileStream, targetFile); //파일 저장
                        //배열에 담기
                    } catch (Exception e) {
                        //파일삭제
                        FileUtils.deleteQuietly(targetFile);    //저장된 현재 파일 삭제
                        e.printStackTrace();
                    }
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return fileInfo;
    }


    /** 멀티파일 업로드 */
    public List<FileInfo> MultiUploadFile(HttpServletRequest request, List<MultipartFile> multipartFile) throws IOException {

        FileDto fileDto = new FileDto();
        List<FileInfo> fileList = new ArrayList<>();

        //파일 시퀀스 리스트
        List<Integer> fileIds = new ArrayList<>();

        String _filePath = String.valueOf(request.getParameter("filePath")).equals("null") ? uploadFilePath : uploadFilePath+String.valueOf(request.getParameter("filePath")+"/");

        try {
            if(multipartFile != null){
                // 파일이 있을때 탄다.
                if(multipartFile.size() > 0 && !multipartFile.get(0).getOriginalFilename().equals("")) {

                    for(MultipartFile file1 : multipartFile) {

                        String originalFileName = file1.getOriginalFilename();    //오리지날 파일명
                        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));    //파일 확장자
                        String savedFileName = UUID.randomUUID() + extension;    //저장될 파일 명

                        File targetFile = new File(_filePath + File.separator + savedFileName);

//                        FileInfo file = new FileInfo();
                        Map<String, Object> file = new HashMap<String, Object>();

                        fileDto = FileDto.builder()
                                .originFileName(originalFileName)
                                .savedFileName(savedFileName)
                                .uploadDir(_filePath)
                                .size(file1.getSize())
                                .contentType(file1.getContentType())
                                .extension(extension)
                                .build();

                        //파일 insert
                        FileInfo fileInfo = fileRepository.save(fileDto.toEntity());

                        try {
                            InputStream fileStream = file1.getInputStream();
                            FileUtils.copyInputStreamToFile(fileStream, targetFile); //파일 저장
                            fileList.add(fileInfo);
                            //배열에 담기
                        } catch (Exception e) {
                            //파일삭제
                            FileUtils.deleteQuietly(targetFile);    //저장된 현재 파일 삭제
                            e.printStackTrace();
                            break;
                        }
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return fileList;
    }


    /**
     * @methodName : deleteFile
     * @date : 2023-05-11 오후 5:13
     * @author : 김재성
     * @Description: 파일 논리삭제
     **/
    public Long deleteFile(Long id){
        Optional<FileInfo> optionalFileInfo = fileRepository.findById(id);
        FileInfo fileInfo = optionalFileInfo.get();
        return fileInfo.getId();
    }

    public void downloadFile(HttpServletResponse res, Long fileIdx) throws Exception {
        //파일 조회
        Optional<FileInfo> fileInfo = fileRepository.findById(fileIdx);

        if(fileInfo.isPresent()){

            FileInfo file = fileInfo.get();

            //파일 경로
            Path saveFilePath = Paths.get(file.getUploadDir() + File.separator + file.getSavedFileName());

            //해당 경로에 파일이 없으면
            if(!saveFilePath.toFile().exists()) {
                throw new RuntimeException("file not found");
            }

            res.setHeader("Content-Disposition", "attachment; filename=\"" +  URLEncoder.encode((String) file.getOriginFileName(), "UTF-8") + "\";");
            res.setHeader("Content-Transfer-Encoding", "binary");
            res.setHeader("Content-Type", "application/download; utf-8");
            res.setHeader("Pragma", "no-cache;");
            res.setHeader("Expires", "-1;");

            FileInputStream fis = null;

            try {
                fis = new FileInputStream(saveFilePath.toFile());
                FileCopyUtils.copy(fis, res.getOutputStream());
                res.getOutputStream().flush();
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
            finally {
                try {
                    fis.close();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else{
            throw new Exception("해당 파일정보가 없습니다.");
        }
    }

    /**
    * @methodName : getThumbnail
    * @date : 2023-05-11 오후 5:17
    * @author : 김재성
    * @Description: 넘어온 파일 인덱스로 썸네일 보여주기
    **/
//    public ResponseEntity<Map<String, String>> getThumbnail(Long fileIdx) throws Exception {
    public ResponseEntity<byte[]> getThumbnail(Long fileIdx) throws Exception {

//        ResponseEntity<Map<String, String>> result = null;
        ResponseEntity<byte[]> result = null;

        Optional<FileInfo> optionalFile = fileRepository.findById(fileIdx);

        if(optionalFile.isPresent()){

            FileInfo fileInfo = optionalFile.get();

            String logiPath = fileInfo.getUploadDir();
            String logiNm = fileInfo.getSavedFileName();

            File file = new File(logiPath+"/"+logiNm);

            try {
//                byte[] imageBytes = Files.readAllBytes(file.toPath());
//                String encodeImage = Base64Utils.encodeToString(imageBytes);
//
//                Map<String, String> response = new HashMap<>();
//                response.put("data", encodeImage);
//
//                result = new ResponseEntity<>(response, HttpStatus.OK);

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
