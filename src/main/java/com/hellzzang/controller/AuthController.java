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

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
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
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

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
    public ResponseEntity<TokenDto> authorize(@Valid @RequestBody LoginDto loginDto) {
        // form 태그 형식으로 데이터를 전송 받으므로 @RequestBody 불필요
        // 이 프로젝트가 아닌 다른 프로젝트에서 form 미 사용 시 붙이면 됨

        //username과 password 파라미터를 받아 객체 생성
//        UsernamePasswordAuthenticationToken authenticationToken =
//                new UsernamePasswordAuthenticationToken(loginDto.getUserid(), loginDto.getPassword());

        //loadUserByUsername 메서드를 통해 유저정보를 조회하여 인증 정보 생성
//        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        //loadUserByUsername 메서드를 호출하지 않았는데 넘어가는 이유
        //authenticationManangerBuilder.getObject().authenticate() 메소드가 실행되면
        //1. AuthenticationManager 의 구현체인 ProviderManager 의 authenticate() 메소드가 실행
        //2. 해당 메소드에선 AuthenticaionProvider 인터페이스의 authenticate() 메소드를 실행하는데,
        //해당 인터페이스에서 데이터베이스에 있는 이용자의 정보를 가져오는 UserDetailsService 인터페이스를 사용
        //3. 이어서 UserDetailsService 인터페이스의 loadUserByUsername() 메소드를 호출하게 됨
        //따라서 CustomUserDetailsService 구현체에 오버라이드된 loadUserByUsername() 메소드를 호출하게 됨

        //위에서 리턴받은 유저 정보와 권한 정보를 인증 정보를 현재 실행중인 스레드(Security Context)에 저장
//        SecurityContextHolder.getContext().setAuthentication(authentication);

        LocalDateTime lastLoginDate = LocalDateTime.now();
        String lastLoginDateFormat = lastLoginDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); //저장될 패턴
        lastLoginDate = LocalDateTime.parse(lastLoginDateFormat, dateFormatter); //String 데이터를 LocalDateTime 형태로 파싱

        // 사용자 정보 조회
        User user = userRepository.findByUserid(loginDto.getUserid());

        //로그인한 사용자가 맞는지 체크
        if(user != null && passwordEncoder.matches(loginDto.getPassword(), user.getPassword())){
            //현재 계정이 block되어 있는지 확인
            if(user.getBlockYn().equals("N")) {
                //유저정보를 통해 jwt토큰 생성
                String jwt = tokenProvider.createToken(user);
                String refreshJwt = tokenProvider.createRefreshToken(user);

                //헤더에 토큰정보를 포함
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

                //마지막 로그인 접속 기록
                user.setLastLoginDate(lastLoginDate);
                //휴면처리 되어있을 경우 해제
                if(user.getDorYn().equals("Y")){
                    user.setDorYn("N");
                }
                userRepository.save(user);

                return new ResponseEntity<>(new TokenDto(jwt, refreshJwt), httpHeaders, HttpStatus.OK);
            }
            else{
                String blockDateFormat = user.getBlockDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH시 mm분"));
                return new ResponseEntity(blockDateFormat+" 까지 이용하실 수 없습니다.", HttpStatus.UNAUTHORIZED);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
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
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(tokenDto.getRefreshToken());

            //토큰에서 사용자 id 가져오기
            String userId = claims.getBody().getSubject();

            //사용자 정보조회
            User user = userRepository.findByUserid(userId);

            //사용자가 있을경우
            if (user != null) {
                //access token 발급
                String jwt = tokenProvider.createToken(user);

                //refresh token 발급
                String refreshJwt = tokenProvider.createRefreshToken(user);

                return ResponseEntity.ok(new TokenDto(jwt, refreshJwt));
            }
        } catch (JwtException | IllegalArgumentException e) {
            // Invalid refresh token
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    /**
     * @methodName : signup
     * @date : 2023-04-19 오후 5:18
     * @author : hj
     * @Description: 회원가입 메서드
     **/
    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity<?> signup(@Valid @RequestBody UserDto userDto, BindingResult result) {
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
    @ResponseBody
    public ResponseEntity<?> userIdCheck(@Valid @RequestBody UserController.userIdCheckDto request, BindingResult result){
        log.info("bindingResult ={}", result);

        if (result.hasErrors()){
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        return ResponseEntity.ok(userService.userIdCheck(request.getUserid()));
    }

    /**
     * @methodName : sendEmail
     * @date : 2023-05-02 오후 4:17
     * @author : hj
     * @Description: 이메일 전송 api, 로그인 시 해당 유저가 존재하지 않으면 해당 api 호출
     **/
    @PostMapping("/sendEmail")
    @ResponseBody
    public void sendEmail(@Valid @RequestBody UserController.userIdCheckDto request) throws MessagingException, IOException {
        userService.sendEmail(request.getUserid());
    }

    /**
     * @methodName : emailCheck
     * @date : 2023-05-03 오후 5:22
     * @author : hj
     * @Description: 회원가입 시 부여된 랜덤 코드를 통해 유저 id 확인
     **/
    @PostMapping("/emailCheck")
    @ResponseBody
    public ResponseEntity<String> emailCheck(@Valid @RequestBody UserController.mailCheckDto request){
        return ResponseEntity.ok(userService.emailCheck(request.getCheckcode()));
    }

}