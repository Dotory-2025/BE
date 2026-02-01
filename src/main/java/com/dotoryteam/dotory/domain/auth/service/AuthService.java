package com.dotoryteam.dotory.domain.auth.service;

import com.dotoryteam.dotory.domain.alarm.entity.FcmToken;
import com.dotoryteam.dotory.domain.alarm.enums.DeviceType;
import com.dotoryteam.dotory.domain.alarm.repository.FcmTokenRepository;
import com.dotoryteam.dotory.domain.auth.dto.response.VerifyResponse;
import com.dotoryteam.dotory.domain.auth.exception.InvalidCodeException;
import com.dotoryteam.dotory.domain.auth.exception.InvalidTokenInAuthException;
import com.dotoryteam.dotory.domain.member.entity.Member;
import com.dotoryteam.dotory.domain.member.exception.MemberNotFoundException;
import com.dotoryteam.dotory.domain.member.repository.MemberRepository;
import com.dotoryteam.dotory.global.redis.service.SecurityRedisService;
import com.dotoryteam.dotory.global.security.dto.JwtTokens;
import com.dotoryteam.dotory.global.security.enums.UserRole;
import com.dotoryteam.dotory.global.security.exception.DifferentTokenException;
import com.dotoryteam.dotory.global.security.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final FcmTokenRepository fcmTokenRepository;
    private final MemberRepository memberRepository;
    private final JwtUtils jwtUtils;
    private final SecurityRedisService securityRedisService;

    private static final String RT_PREFIX = "RT:";
    private static final String BL_PREFIX = "BL:";
    private static final String AUTH_CODE_PREFIX = "CODE:";
    private static final String SIGN_UP_PREFIX = "SIGNUP:";


    @Value("${spring.jwt.tokens.expire.refresh_token}")
    private Long refreshTokenExpire;

    @Value("${spring.jwt.tokens.expire.access_token}")
    private Long accessTokenExpire;

    @Transactional
    public VerifyResponse verify(String email , String code , String fcmToken , DeviceType deviceType) {
        String savedCode = securityRedisService.getValue(AUTH_CODE_PREFIX + email);

        //만료된 인증번호
        if (savedCode == null) {
            throw new InvalidCodeException(HttpStatus.BAD_REQUEST , "인증번호가 만료되었습니다.");
        }

        //인증번호가 다름
        if (!savedCode.equals(code)) {
            throw new InvalidCodeException(HttpStatus.BAD_REQUEST , "인증번호가 일치하지 않습니다.");
        }

        Optional<Member> optionalMember = memberRepository.findByEmail(email);

        //가입 된 사용자인지 아닌지 여부
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();

            JwtTokens tokens = jwtUtils.generateTokens(email , UserRole.USER);
            securityRedisService.setValue(
                    RT_PREFIX + email ,
                    tokens.getRefreshToken() ,
                    refreshTokenExpire
            );

            if (!fcmTokenRepository.existsByFcmToken(fcmToken)) {       //  알림 도메인 커밋할 때 같이
                FcmToken newFcmToken = FcmToken.builder()
                        .member(member)
                        .fcmToken(fcmToken)
                        .deviceType(deviceType)
                        .build();
                fcmTokenRepository.save(newFcmToken);
            }

            return VerifyResponse.builder()
                    .isMember(true)
                    .jwtTokens(tokens)
                    .signupToken(null)
                    .build();
        } else {
            String signupToken = UUID.randomUUID().toString();
            //멤버가 아니면 회원가입 후 토큰 발부를 위한 임시 출입증 발급
            securityRedisService.setValue(SIGN_UP_PREFIX + email , signupToken , 1800000L);

            return VerifyResponse.builder()
                    .isMember(false)
                    .jwtTokens(null)
                    .signupToken(signupToken)
                    .build();
        }
    }

    @Transactional
    public void logout(String requestAccessTokenInHeader) {
        String accessToken = resolveToken(requestAccessTokenInHeader);

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
    public JwtTokens reissue(String requestRefreshToken) {
        String refreshToken = resolveToken(requestRefreshToken);

        // 1. 토큰 유효성 검사 (이제 순수 토큰이라 검증 가능)
        if (!jwtUtils.validateToken(refreshToken)) {
            throw new InvalidTokenInAuthException();
        }

        // 2. 이메일 추출
        String email = jwtUtils.getUserEmail(refreshToken);

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);

        String savedToken = securityRedisService.getValue(RT_PREFIX + email);

        if (!refreshToken.equals(savedToken)) {
            throw new DifferentTokenException();
        }

        JwtTokens tokens = jwtUtils.generateTokens(
                member.getEmailVerification().getEmail() ,
                member.getUserRole()
        );

        // 6. Redis 업데이트 (덮어쓰기)
        securityRedisService.setValue(
                RT_PREFIX + email
                , tokens.getRefreshToken()
                , refreshTokenExpire
        );

        return tokens;
    }

    private String resolveToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return token; // Bearer가 없으면 그냥 반환 (혹은 예외처리)
    }

    @Transactional
    public void deleteMember(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);

        // 2. Redis에 저장된 리프레시 토큰 삭제 (뒷정리)
        securityRedisService.deleteValue(RT_PREFIX + email);

        // 3. DB 삭제 (Member 엔티티에 Cascade 설정이 되어 있어야 함)
        memberRepository.delete(member);
    }

}
