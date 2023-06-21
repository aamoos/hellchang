package com.hellzzang.oauth2;

/**
 * packageName    : com.hellzzang.oauth2
 * fileName       : CustomOAuth2UserService
 * author         : 김재성
 * date           : 2023-06-21
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-06-21        김재성       최초 생성
 */
import com.hellzzang.entity.Authority;
import com.hellzzang.entity.User;
import com.hellzzang.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.*;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final HttpSession session;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        // 현재 로그인 진행 중인 서비스를 구분하는 코드
        String registrationId = userRequest
                .getClientRegistration()
                .getRegistrationId();
        // oauth2 로그인 진행 시 키가 되는 필드값
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();
        // OAuthAttributes: attribute를 담을 클래스 (개발자가 생성)
        OAuthAttributes attributes = OAuthAttributes
                .of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        User user = saveOrUpdate(attributes, registrationId);
        // SessioUser: 세션에 사용자 정보를 저장하기 위한 DTO 클래스 (개발자가 생성)

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }
    private User saveOrUpdate(OAuthAttributes attributes, String registrationId) {
        User user = userRepository.findByUserid(attributes.getEmail());
        if(user !=null){
            user = userRepository.findByUserid(attributes.getEmail());
        }
        else {
            user=attributes.toEntity();
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            //임시비멀번호 설정
            user.setPassword(passwordEncoder.encode(user.getUserid()));

            String socialId = "";

            if("kakao".equals(registrationId)){
                socialId = String.valueOf(attributes.getAttributes().get("id"));
            }else if("naver".equals(registrationId)){
                LinkedHashMap map = (LinkedHashMap) attributes.getAttributes().get("response");
                socialId = String.valueOf(map.get("id"));
            }else if("google".equals(registrationId)){
                Map map = attributes.getAttributes();
                socialId = String.valueOf(map.get("sub"));
            }

            user.setSocialId(socialId);

            //저장
            Long id = userRepository.save(user).getId();
            user = userRepository.findById(id).get();
        }

        return user;
    }
}
