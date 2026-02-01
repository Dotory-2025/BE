package com.dotoryteam.dotory.global.security.config;

import com.dotoryteam.dotory.global.redis.service.SecurityRedisService;
import com.dotoryteam.dotory.global.security.enums.UserRole;
import com.dotoryteam.dotory.global.security.filter.JwtAuthenticateFilter;
import com.dotoryteam.dotory.global.security.handler.CustomAuthenticationEntryPoint;
import com.dotoryteam.dotory.global.security.utils.CustomUserDetailsService;
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
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomUserDetailsService customUserDetailsService;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/api/v1/auth/login"    //  로그인
                                , "/api/v1/auth/reissue"
                                , "/api/v1/email-verification/**"   //  이메일 인증번호 발송 & 검증
                                , "/api/v1/members/join"     //  회원가입
                                , "/api/v1/members/email-duplicate"
                                , "/api/v1/members/nickname-duplicate"
                                , "/api/v1/auth/delete"

                                , "/api/v1/members/switch"

                                , "/swagger-ui/**"      //  swagger 사용
                                , "/v3/api-docs/**"
                                , "/swagger-ui.html"
                        ).permitAll()
                        .requestMatchers("/api/v1/notifications/**").authenticated()
                        .requestMatchers("/api/v1/admin/**").hasRole(UserRole.ADMIN.toString())
                        .anyRequest().hasAnyRole(UserRole.USER.toString() , UserRole.ADMIN.toString())
                )
                //예외 등록할 entry point 설정
                .exceptionHandling(handler -> handler.authenticationEntryPoint(customAuthenticationEntryPoint))
                .addFilterBefore(
                        new JwtAuthenticateFilter(jwtUtils , securityRedisService , customUserDetailsService) ,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}
