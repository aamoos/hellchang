package com.hellzzang.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * packageName    : com.hellzzangAdmin.entity
 * fileName       : File
 * author         : 김재성
 * date           : 2023-05-11
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-05-11        김재성       최초 생성
 */

@Table(name = "file")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class FileInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                    //id

    @Column(nullable = false)
    private String originFileName;      //원본 파일명

    @Column(nullable = false)
    private String savedFileName;       //저장된 파일명

    private String uploadDir;           //경로명

    private String extension;           //확장자

    private Long size;                  //파일 사이즈

    private String contentType;         //ContentType

    @CreatedDate
    private LocalDateTime regDate;     //등록 날짜

    private String delYn;               //삭제여부

    @Transient
    private String url;        //썸네일 url

    @Builder
    public FileInfo(Long id, String originFileName, String savedFileName
            , String uploadDir, String extension, Long size, String contentType, String delYn){
        this.id = id;
        this.originFileName = originFileName;
        this.savedFileName = savedFileName;
        this.uploadDir = uploadDir;
        this.extension = extension;
        this.size = size;
        this.contentType = contentType;
        this.delYn = delYn;
    }

    /**
    * @methodName : updateDelYn
    * @date : 2023-05-22 오전 10:07
    * @author : 김재성
    * @Description: 삭제여부 업데이트
    **/
    public void updateDelYn(String delYn){
        this.delYn = delYn;
    }

    /**
    * @methodName : updateUrl
    * @date : 2023-05-22 오전 10:07
    * @author : 김재성
    * @Description: 썸네일 url 업데이트
    **/
    public void updateUrl(String url){
        this.url = url;
    }
}