package com.hellzzang.config;

import com.hellzzang.jwt.JwtAccessDeniedHandler;
import com.hellzzang.jwt.JwtAuthenticationEntryPoint;
import com.hellzzang.jwt.JwtSecurityConfig;
import com.hellzzang.jwt.TokenProvider;
import com.hellzzang.oauth2.CustomOAuth2UserService;
import com.hellzzang.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.hellzzang.oauth2.OAuth2AuthenticationFailureHandler;
import com.hellzzang.oauth2.OAuth2AuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
* @package : com.hellzzang.config
* @name : SecurityConfig.java
* @date : 2023-09-25 오후 3:09
* @author : 김재성
* @Description: Security 설정파일
**/
@EnableWebSecurity //기본적인 Web 보안 활성화
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    public SecurityConfig(
            TokenProvider tokenProvider,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            JwtAccessDeniedHandler jwtAccessDeniedHandler,
            CustomOAuth2UserService customOAuth2UserService,
            OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler,
            OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler) {
        this.tokenProvider = tokenProvider;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
        this.customOAuth2UserService = customOAuth2UserService;
        this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
        this.oAuth2AuthenticationFailureHandler = oAuth2AuthenticationFailureHandler;
    }

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieOAuth2AuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) ->
                web
                        .ignoring()
                        .antMatchers(  //아래의 요청들에 대해서는 Spring Security 로직을 수행하지 않아도 접근이 가능(=인증 무시)
                                "/h2-console/**"
                                , "/favicon.ico"
                                , "/css/**", "/js/**", "/img/**"
                        );
    }

    //WebSecurityConfigurerAdapter는 더이상 사용되지 않아 SecurityFilterChain을 Bean으로 등록하여 사용
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()  //token 방식이므로 csrf 설정 x

                .exceptionHandling() //예외처리 지정 401, 403
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                .and() // 데이터 확인을 위한 h2 console 설정 추가 -> 추가적인 서칭 필요
                .headers()
                .frameOptions()
                .sameOrigin()

                .and() //Security는 기본적으로 세션을 사용하지만 jwt는 세션을 사용하지 않으므로 STATELESS로 설정
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()  //요청들에 대한 접근 설정
                .antMatchers("/auth/**", "/oauth2/**", "/userJoin/**", "/thumbnail/**").permitAll()
                .anyRequest().authenticated()  //이외 나머지 요청은 인증이 필요
                .and()
                .oauth2Login()
                .authorizationEndpoint().baseUri("/oauth2/authorize")
                .authorizationRequestRepository(cookieOAuth2AuthorizationRequestRepository())
                .and()
                .redirectionEndpoint()
                .baseUri("/login/oauth2/code/**")
                .and()
                .userInfoEndpoint().userService(customOAuth2UserService)
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler)
                .and()
                // JwtFilter를 addFilterBefore로 등록했던 JwtSecurityConfig 클래스를 적용
                .apply(new JwtSecurityConfig(tokenProvider));

//                .and() // JwtFilter를 addFilterBefore로 등록했던 JwtSecurityConfig 클래스를 적용
//                    .apply(new JwtSecurityConfig(tokenProvider))
//                .and()
//                    .oauth2Login().userInfoEndpoint().userService(cusoAuth2UserService);
        return http.build();

    }
}
