package com.dotoryteam.dotory.global.security.config;

import com.dotoryteam.dotory.global.redis.service.SecurityRedisService;
import com.dotoryteam.dotory.global.security.enums.UserRole;
import com.dotoryteam.dotory.global.security.filter.JwtAuthenticateFilter;
import com.dotoryteam.dotory.global.security.handler.AuthenticationEntryPoint;
import com.dotoryteam.dotory.global.security.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {
    private final JwtUtils jwtUtils;
    private final SecurityRedisService securityRedisService;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        //role 처리 어떻게 할지 고민....
                        .requestMatchers(
                                "/api/v1/auth/login"    //  로그인
                                , "/api/v1/email-verification/**"   //  이메일 인증번호 발송 & 검증
                                , "/api/v1/member/join"     //  회원가입
                                , "/api/v1/member/email-duplicate"
                                , "/api/v1/member/nickname-duplicate"

                                , "/swagger-ui/**"      //  swagger 사용
                                , "/v3/api-docs/**"
                                , "/swagger-ui.html"
                        ).permitAll()
                        .requestMatchers("/api/v1/notifications/**").authenticated()
                        .requestMatchers("/api/v1/admin/**").hasRole(UserRole.ROLE_ADMIN.toString())
                        .anyRequest().hasAnyRole(UserRole.ROLE_USER.toString() , UserRole.ROLE_ADMIN.toString())
                )
                //예외 등록할 entry point 설정
                .exceptionHandling(handler -> handler.authenticationEntryPoint(new AuthenticationEntryPoint()))
                .addFilterBefore(
                        new JwtAuthenticateFilter(jwtUtils , securityRedisService) ,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}
