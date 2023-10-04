package com.hellzzang.controller;

import com.hellzzang.common.ValidationErrorResponse;
import com.hellzzang.dto.LoginDto;
import com.hellzzang.dto.TokenDto;
import com.hellzzang.dto.UserDto;
import com.hellzzang.entity.User;
import com.hellzzang.jwt.JwtFilter;
import com.hellzzang.jwt.TokenProvider;
import com.hellzzang.repository.UserRepository;
import com.hellzzang.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @package : com.example.jwt.controller
 * @name : AuthController.java
 * @date : 2023-04-19 오후 5:06
 * @author : hj
 * @Description: 권한 관련 클래스
 **/
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    @Value("${jwt.secret}")
    private String secret;

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    /**
     * @methodName : authorize
     * @date : 2023-04-19 오후 5:06
     * @author : hj
     * @Description: 로그인 시 토큰 발급하는 메서드
     **/

    @CrossOrigin
    @PostMapping("/authenticate")
    public ResponseEntity<?> authorize(@Valid @RequestBody LoginDto loginDto) {
        try {
            User user = userRepository.findByUserId(loginDto.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

            if ("N".equals(user.getBlockYn()) && passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
                String token = tokenProvider.createToken(user);
                String refreshToken = tokenProvider.createRefreshToken(user);

                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + token);

                return ResponseEntity.ok(new TokenDto(token, refreshToken));
            } else if ("Y".equals(user.getBlockYn())) {
                String blockDateFormat = user.getBlockDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH시 mm분"));
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(blockDateFormat + " 까지 이용하실 수 없습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
    * @methodName : refresh
    * @date : 2023-05-08 오후 4:31
    * @author : 김재성
    * @Description: refresh 토큰 발급
    **/
    @PostMapping("/refresh")
    public ResponseEntity<TokenDto> refresh(@RequestBody TokenDto tokenDto) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(tokenDto.getRefreshToken());

            String userId = claims.getBody().getSubject();

            User user = userRepository.findByUserId(userId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

            // Access token 발급
            String accessToken = tokenProvider.createToken(user);

            // Refresh token 발급
            String refreshToken = tokenProvider.createRefreshToken(user);

            TokenDto newTokenDto = new TokenDto(accessToken, refreshToken);

            return ResponseEntity.ok(newTokenDto);
        } catch (JwtException | IllegalArgumentException e) {
            // Invalid refresh token
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * @methodName : signup
     * @date : 2023-04-19 오후 5:18
     * @author : hj
     * @Description: 회원가입 메서드
     **/
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody UserDto userDto, BindingResult result) throws Exception {
        log.info("bindingResult ={}", result);

        if (result.hasErrors()) {
            Map<String, List<String>> fieldErrors = new HashMap<>();
            result.getFieldErrors().forEach(fieldError -> {
                String field = fieldError.getField();
                String message = fieldError.getDefaultMessage();
                fieldErrors.computeIfAbsent(field, key -> new ArrayList<>()).add(message);
            });

            return ResponseEntity.badRequest().body(new ValidationErrorResponse(fieldErrors));
        }

        return ResponseEntity.ok(userService.signup(userDto)); //http의 body, header, status를 포함한 데이터 -> 추가 서칭 필요
        //Response header에는 웹서버가 웹브라우저에 응답하는 메시지가 들어있음
        //Reponse body에는 데이터 값이 들어있음
    }

    /**
     * @methodName : userIdCheck
     * @date : 2023-05-02 오후 3:23
     * @author : hj
     * @Description: userid입력 후 로그인 시 로그인 or 회원가입
     **/
    @PostMapping("/userExistenceCheck")
    public ResponseEntity<?> userIdCheck(@RequestBody UserIdCheckDto userIdCheckDto, BindingResult result){
        log.info("bindingResult ={}", result);
        return ResponseEntity.ok(userService.userIdCheck(userIdCheckDto.getUserId()));
    }

    /**
     * @methodName : sendEmail
     * @date : 2023-05-02 오후 4:17
     * @author : hj
     * @Description: 이메일 전송 api, 로그인 시 해당 유저가 존재하지 않으면 해당 api 호출
     **/
    @PostMapping("/sendEmail")
    public void sendEmail(@RequestBody UserIdCheckDto userIdCheckDto) throws Exception {
        userService.sendEmail(userIdCheckDto.getUserId());
    }

    /**
     * @methodName : emailCheck
     * @date : 2023-05-03 오후 5:22
     * @author : hj
     * @Description: 회원가입 시 부여된 랜덤 코드를 통해 유저 id 확인
     **/
    @PostMapping("/emailCheck")
    public ResponseEntity<String> emailCheck(@RequestBody EmailCheckDto emailCheckDto){
        return ResponseEntity.ok(userService.emailCheck(emailCheckDto.getCheckCode()));
    }

    @Data
    static class UserIdCheckDto{
        private String userId;
    }

    @Data
    static class EmailCheckDto{
        private String checkCode;
    }

}