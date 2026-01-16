package com.dotoryteam.dotory.domain.auth.service;

import com.dotoryteam.dotory.domain.auth.controller.AuthController;
import com.dotoryteam.dotory.domain.auth.dto.response.VerifyResponse;
import com.dotoryteam.dotory.domain.auth.entity.EmailVerification;
import com.dotoryteam.dotory.domain.auth.exception.CustomAuthException;
import com.dotoryteam.dotory.domain.member.repository.MemberRepository;
import com.dotoryteam.dotory.global.redis.service.SecurityRedisService;
import com.dotoryteam.dotory.global.security.dto.JwtTokens;
import com.dotoryteam.dotory.global.security.enums.ErrorCode;
import com.dotoryteam.dotory.global.security.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.aot.hint.JdkProxyHintExtensionsKt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final JwtUtils jwtUtils;
    private final SecurityRedisService securityRedisService;

    private static final String RT_PREFIX = "RT:";
    private static final String BL_PREFIX = "BL:";
    private static final String AUTH_CODE_PREFIX = "AUTH_CODE:";


    @Value("${spring.jwt.tokens.expire.refresh_token}")
    private Long refreshTokenExpire;

    @Value("${spring.jwt.tokens.expire.access_token}")
    private Long accessTokenExpire;

    @Transactional
    public VerifyResponse verify(String email , String code) {
        String savedCode = securityRedisService.getValue("CODE" + email);

        //만료된 인증번호
        if (savedCode == null) {
            throw new CustomAuthException(ErrorCode.EXPIRED_AUTH_CODE);
        }

        //인증번호가 다름
        if (!savedCode.equals(code)) {
            throw new CustomAuthException(ErrorCode.INVALID_AUTH_CODE);
        }

        if (memberRepository.findByEmail(email).isPresent()) {
            JwtTokens tokens = jwtUtils.generateTokens(email);
            securityRedisService.setValue(
                    RT_PREFIX + email ,
                    tokens.getRefreshToken() ,
                    refreshTokenExpire
            );

            return VerifyResponse.builder()
                    .isMember(true)
                    .jwtTokens(tokens)
                    .signupToken(null)
                    .build();
        } else {
            String signupToken = UUID.randomUUID().toString();
            //멤버가 아니면 회원가입 후 토큰 발부를 위한 임시 출입증 발급
            securityRedisService.setValue("SIGNUP" + signupToken , email , 1800000L);

            return VerifyResponse.builder()
                    .isMember(false)
                    .jwtTokens(null)
                    .signupToken(signupToken)
                    .build();
        }
    }

    @Transactional
    public void logout(String accessToken) {
        Authentication authentication = jwtUtils.getAuthentication(accessToken);
        String email = authentication.getName();
        //refreshToken 삭제
        securityRedisService.deleteValue(RT_PREFIX + email);
        //access token 블랙 리스트 등록
        securityRedisService.setBlackList(
                BL_PREFIX + accessToken
                , "logout"
                , accessTokenExpire
        );
    }

    @Transactional
    public JwtTokens reissue(String refreshToken) {
        //재발급을 위해 기존 토큰 유효성 검사
            //토큰 만료 여부 , 형태가 이상한 토큰 검증
        if (!jwtUtils.validateToken(refreshToken)) {
            throw new CustomAuthException(ErrorCode.INVALID_TOKEN);
        }

        String email = jwtUtils.getAuthentication(refreshToken).getName();
        String savedToken = securityRedisService.getValue(RT_PREFIX + email);

        //기존 토큰과 redis 에 저장된 토큰값 비교하기
        if (!refreshToken.equals(savedToken)) {
            //토큰이 기존 발급받았던 토큰이랑 같지 않으면 exception 던지기
            throw new CustomAuthException(ErrorCode.INVALID_TOKEN);
        }

        JwtTokens tokens = jwtUtils.generateTokens(email);

        //새 토큰으로 덮어쓰기 (이러면 기존 refresh token 을 삭제하는 코드가 필요없음)
        securityRedisService.setValue(
                RT_PREFIX + email
                , tokens.getRefreshToken()
                , refreshTokenExpire
        );

        return tokens;
    }

}
