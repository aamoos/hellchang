package com.hellzzang.dto.myInfo;

import com.hellzzang.entity.User;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

/**
 * packageName    : com.hellzzang.dto.user
 * fileName       : userResponseDto
 * author         : 김재성
 * date           : 2023-10-18
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-10-18        김재성       최초 생성
 */

@Data
public class MyInfoResponseDto {
    private String userName;    //이름
    private String userId;      //아이디
    private String nickName;    //닉네임
    private String address;     //주소
    private String addressDetail;   //상세주소
    private String phone;           //핸드폰번호
    private String thumbnailUrl;

    public MyInfoResponseDto(User user, String thumbnailUrl) {
        this.userName = user.getUserName();
        this.userId = user.getUserId();
        this.nickName = user.getNickName();
        this.address = user.getAddress();
        this.addressDetail = user.getAddressDetail();
        this.phone = user.getPhone();
        this.thumbnailUrl = thumbnailUrl + user.getThumbnailIdx();
    }
}
