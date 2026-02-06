package com.dotoryteam.dotory.global.security.utils;

import com.dotoryteam.dotory.global.security.dto.JwtTokens;
import com.dotoryteam.dotory.global.security.enums.UserRole;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class JwtUtils {
    private final CustomUserDetailsService userDetailsService;
    //yml
    @Value("${spring.jwt.secret}")
    private String JWT_SECRET_KEY ;

    //yml
    @Value("${spring.jwt.tokens.expire.access_token}")
    private Long ACCESS_TOKEN_EXPIRE_TIME;

    @Value("${spring.jwt.tokens.expire.refresh_token}")
    private Long REFRESH_TOKEN_EXPIRE_TIME;


    public JwtTokens generateTokens(String email, UserRole role) {
        Long now = (new Date()).getTime();

        // 1. 역할(Role)을 바로 문자열로 변환 (불필요한 객체 생성 X)
        String authority = "ROLE_" + role.toString();

        // 2. Access Token 생성
        Date accessTokenExpiryTime = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .claim("auth", authority) // ★ 확실하게 들어감
                .expiration(accessTokenExpiryTime)
                .signWith(Keys.hmacShaKeyFor(JWT_SECRET_KEY.getBytes()))
                .compact();

        // 3. Refresh Token 생성
        Date refreshTokenExpiryTime = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);
        String refreshToken = Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(refreshTokenExpiryTime)
                .signWith(Keys.hmacShaKeyFor(JWT_SECRET_KEY.getBytes()))
                .compact();

        return JwtTokens.builder()
                .type("Bearer ")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    //객체 반환
    public Authentication getAuthentication(String token) {
        String email = getUserEmail(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(
                userDetails ,
                "" ,
                userDetails.getAuthorities()
        );
    }

    public String getUserEmail(String token) {
        return parseClaims(token).getSubject();
    }

    //service 레이어 전용 검증 로직 (서비스 레이어에선 T/F 여부만 필요)
        //사용자에게 받은 토큰을 다시 한번 검증하는 방식이 필요
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(JWT_SECRET_KEY.getBytes()))
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(JWT_SECRET_KEY.getBytes()))
                    .build()
                    .parseSignedClaims(accessToken)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            // 만료된 토큰이어도 클레임(정보)은 반환
            return e.getClaims();
        }
    }
}
