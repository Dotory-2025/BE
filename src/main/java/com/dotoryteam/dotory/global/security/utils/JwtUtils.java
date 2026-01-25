package com.dotoryteam.dotory.global.security.utils;

import com.dotoryteam.dotory.global.security.dto.JwtTokens;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtUtils {
    //yml
    @Value("${spring.jwt.secret}")
    private String JWT_SECRET_KEY ;

    //yml
    @Value("${spring.jwt.tokens.expire.access_token}")
    private Long ACCESS_TOKEN_EXPIRE_TIME;

    @Value("${spring.jwt.tokens.expire.refresh_token}")
    private Long REFRESH_TOKEN_EXPIRE_TIME;

    public JwtTokens generateTokens(String email) {
        Long now = (new Date()).getTime();

        //권한 하드코딩 부분 추후에 수정할 예정
        List<GrantedAuthority> authorities =
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

        Authentication authentication
                = new UsernamePasswordAuthenticationToken(email , null , authorities);

        Date accessTokenExpiryTime = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        String getAuthority = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String accessToken = Jwts.builder()
                .subject(authentication.getName())
                .issuedAt(new Date())
                .claim("auth", getAuthority)
                .expiration(accessTokenExpiryTime)
                .signWith(Keys.hmacShaKeyFor(JWT_SECRET_KEY.getBytes()))
                .compact();

        Date refreshTokenExpiryTime = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);
        String refreshToken = Jwts.builder()
                .subject(authentication.getName())
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
        Claims claims = parseClaims(token);

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString()
                                .split(","))
                        .map(SimpleGrantedAuthority::new)
                        .toList();

        //detail 뽑아오기
        UserDetails principal =
                new User(claims.getSubject() , "" , authorities);

        return new UsernamePasswordAuthenticationToken(principal , "", authorities);
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
