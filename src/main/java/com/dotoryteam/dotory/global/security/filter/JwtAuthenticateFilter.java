package com.dotoryteam.dotory.global.security.filter;

import com.dotoryteam.dotory.global.redis.service.SecurityRedisService;
import com.dotoryteam.dotory.global.security.exception.CustomSecurityException;
import com.dotoryteam.dotory.global.security.exception.CustomExpiredJwtTokenException;
import com.dotoryteam.dotory.global.security.exception.InvalidJwtTokenException;
import com.dotoryteam.dotory.global.security.exception.LoggedOutTokenException;
import com.dotoryteam.dotory.global.security.utils.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticateFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final SecurityRedisService redisService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request ,
            @NonNull HttpServletResponse response ,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        String token = resolveToken(request);

        try {
            if (token != null) {

                //블랙리스트에 넣기
                if (!redisService.isBlackList("BL:" + token)) {
                    Authentication authentication = jwtUtils.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else
                    throw new LoggedOutTokenException();
            }
        } catch (ExpiredJwtException e) {
            request.setAttribute("exception" , new CustomExpiredJwtTokenException(HttpStatus.UNAUTHORIZED , "만료된 토큰입니다."));
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            request.setAttribute("exception" , new InvalidJwtTokenException(HttpStatus.UNAUTHORIZED , "유효하지 않은 토큰입니다."));
        } catch (LoggedOutTokenException e) {
            request.setAttribute("exception" , e);
        } catch (Exception e) {
            request.setAttribute("exception" , new CustomSecurityException(HttpStatus.INTERNAL_SERVER_ERROR , "서버 내부 오류입니다."));
        }
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}
