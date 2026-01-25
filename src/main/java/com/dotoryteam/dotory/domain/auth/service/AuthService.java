package com.dotoryteam.dotory.domain.auth.service;

import com.dotoryteam.dotory.domain.alarm.entity.FcmToken;
import com.dotoryteam.dotory.domain.alarm.enums.DeviceType;
import com.dotoryteam.dotory.domain.alarm.repository.FcmTokenRepository;
import com.dotoryteam.dotory.domain.auth.dto.response.VerifyResponse;
import com.dotoryteam.dotory.domain.auth.exception.InvalidCodeException;
import com.dotoryteam.dotory.domain.auth.exception.InvalidTokenInAuthException;
import com.dotoryteam.dotory.domain.member.exception.MemberNotFoundException;
import com.dotoryteam.dotory.domain.member.repository.MemberRepository;
import com.dotoryteam.dotory.global.redis.service.SecurityRedisService;
import com.dotoryteam.dotory.global.security.dto.JwtTokens;
import com.dotoryteam.dotory.global.security.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        //fcm 토큰 발급된 적이 있는지 판별
        if (!fcmTokenRepository.existsByFcmToken(fcmToken)) {
            FcmToken newFcmToken = FcmToken.builder()
                    .member(memberRepository.findByEmail(email).orElseThrow(()
                            ->  new MemberNotFoundException(HttpStatus.NOT_FOUND , "해당 사용자를 찾을 수 없습니다.")))
                    .fcmToken(fcmToken)
                    .deviceType(deviceType)
                    .build();
            fcmTokenRepository.save(newFcmToken);
        }

        //가입 된 사용자인지 아닌지 여부
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
            securityRedisService.setValue(SIGN_UP_PREFIX + signupToken , email , 1800000L);

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
            throw new InvalidTokenInAuthException(HttpStatus.BAD_REQUEST , "유효하지 않은 토큰입니다.");
        }

        String email = jwtUtils.getAuthentication(refreshToken).getName();
        String savedToken = securityRedisService.getValue(RT_PREFIX + email);

        //기존 토큰과 redis 에 저장된 토큰값 비교하기
        if (!refreshToken.equals(savedToken)) {
            //토큰이 기존 발급받았던 토큰이랑 같지 않으면 exception 던지기
            throw new InvalidTokenInAuthException(HttpStatus.BAD_REQUEST , "유효하지 않은 토큰입니다.");
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
