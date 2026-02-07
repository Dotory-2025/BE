package com.dotoryteam.dotory.domain.member.service;

import com.dotoryteam.dotory.domain.lifestyle.entity.Lifestyle;
import com.dotoryteam.dotory.domain.lifestyle.service.LifestyleService;
import com.dotoryteam.dotory.domain.member.dto.request.JoinReq;
import com.dotoryteam.dotory.domain.member.dto.request.UpdateMemberDetailsReq;
import com.dotoryteam.dotory.domain.member.dto.response.MemberDetailRes;
import com.dotoryteam.dotory.domain.member.dto.response.MemberSearchRes;
import com.dotoryteam.dotory.domain.member.entity.Member;
import com.dotoryteam.dotory.domain.member.enums.UserStatus;
import com.dotoryteam.dotory.domain.member.exception.*;
import com.dotoryteam.dotory.domain.member.repository.MemberRepository;
import com.dotoryteam.dotory.domain.member.utils.NicknameKeywordParser;
import com.dotoryteam.dotory.global.common.dto.CursorResult;
import com.dotoryteam.dotory.global.image.service.S3Service;
import com.dotoryteam.dotory.global.image.utils.S3UrlGenerator;
import com.dotoryteam.dotory.global.redis.service.SecurityRedisService;
import com.dotoryteam.dotory.global.security.dto.JwtTokens;
import com.dotoryteam.dotory.global.security.enums.UserRole;
import com.dotoryteam.dotory.global.security.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    private final JwtUtils jwtUtils;
    private final LifestyleService lifestyleService;
    private final SecurityRedisService securityRedisService;
    private final MemberDummyData dummyData;
    private final NicknameKeywordParser memberKeywordParser;
    private final S3Service s3Service;
    private final S3UrlGenerator urlGenerator;

    private static final String AUTH_CODE_PREFIX = "SIGNUP:";
    private static final String RT_PREFIX = "RT:";
    private static final String BL_PREFIX = "BL:";

    @Value("${spring.jwt.tokens.expire.refresh_token}")
    private Long refreshTokenExpire;

    @Value("${spring.jwt.tokens.expire.access_token}")
    private Long accessTokenExpire;

    private static final String allowedRegex = "[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ\\u3040-\\u309F\\u30A0-\\u30FF\\u4E00-\\u9FFF\\uD800-\\uDFFF\\s]*";

    /**
     * 사용자 가입 로직
     * @param joinReq: 가입에 필요한 값들 ( 닉네임 , 입학년도 , 선호 기숙사 , 프로필 사진 Url , 성별 , 이메일 )과 가입이 필요한 사용자 검증용 signupToken
     * @return: 토큰
     */
    public JwtTokens join(JoinReq joinReq) {
        if (joinReq.getSignUpToken() == null || joinReq.getSignUpToken().isEmpty()) {
            throw new InvalidJoinRequestException();
        }

        if (!joinReq.getSignUpToken().equals(
                securityRedisService.getValue(AUTH_CODE_PREFIX + joinReq.getEmail()))) {
            //이전 요청이 사라지지 않고 지속적으로 문제를 발생
            securityRedisService.deleteValue(AUTH_CODE_PREFIX + joinReq.getEmail());
            throw new JoinRequestExpiredException();
        }

        //생활패턴 타입 변경
        List<Lifestyle> lifestyles = lifestyleService.convert(joinReq.getLifestyleCodes());

        //lifestyles 는 member 객체에 의해 Lifestyle -> MemberLifestyle 로 변경되어져서 들어감
        //***** 추후에 lifestyle 빈 리스트 넘어오면 예외 넘겨받게 수정 예정
        //프론트에서 받을 때 String 으로 받을 수 밖에 없어서 변환이 필수
        Member member = Member.builder()
                //이메일 중복 검사를 통해 이미 저장 가능한 형태의 이메일임을 확신한 상태
                .nickname(joinReq.getNickname())
                .entranceYear(joinReq.getEntranceYear())
                .sex(joinReq.getSex())
                .email(joinReq.getEmail())
                .lifestyles(lifestyles)
                .profileImgUrl(joinReq.getProfileImgUrl())
                .build();
        memberRepository.save(member);

        JwtTokens tokens = jwtUtils.generateTokens(
                joinReq.getEmail() ,
                UserRole.USER
        );

        securityRedisService.setValue(
                RT_PREFIX + joinReq.getEmail()
                , tokens.getRefreshToken()
                , refreshTokenExpire
        );

        securityRedisService.deleteValue(AUTH_CODE_PREFIX + joinReq.getEmail());

        return tokens;
    }

    /**
     * 사용자 검색
     * @param myMemberKey: 내 memberKey (사용자 검색 시 나를 제외한 결과를 출력하기 위함)
     * @param nicknames: 검색 닉네임 (다수 검색일 수도 있어 parser 사용해서 List 로 변환 후 검색)
     * @param minEntrance: 입학년도 최소치
     * @param maxEntrance: 입학년도 최대치
     * @param lifestyleCodes: 생활패턴들
     * @param cursor: 커서
     * @param size: 사이지
     * @return: MemberDetailRes
     */
    public CursorResult<MemberSearchRes> search(
            UUID myMemberKey ,
            String nicknames ,
            Long minEntrance ,
            Long maxEntrance ,
            List<String> lifestyleCodes ,
            String cursor ,
            int size
    ) {
        List<Lifestyle> lifestyles = lifestyleService.convert(lifestyleCodes);

        return memberRepository.searchMembers(
                myMemberKey , memberKeywordParser.keywordParser(nicknames) , minEntrance , maxEntrance , lifestyles , cursor , size
        );
    }

    /**
     * 닉네임 중복 여부 확인 로직
     * @param nickname: 닉네임 중복 확인 시 특수문자 존재 여부 확인용 ( allowedRegex: 사용 가능한 글자 범위 - 한글 , 영어 , 일본어 , 중국어 , 이모티콘 )
     */
    public void isNicknameDuplicated(String nickname) {
        if (nickname == null || nickname.isBlank()) {
            throw new InvalidNicknameFormatException();
        }

        if (!nickname.matches(allowedRegex)) {
            throw new InvalidNicknameFormatException();
        }

        if (memberRepository.nicknameDuplicate(nickname)) {
            throw new AlreadyExistNicknameException();
        }
    }


    /**
     * 해당 멤버의 디테일 정보 보기
     * @param id: 해당 멤버의 PK
     */
    public MemberDetailRes getMyDetail(long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);

        //형 변환 먼저 들어가기
        List<String> lifestyles = member.getMemberLifestyle().stream()
                .map(ml -> ml.getLifestyle()
                        .getName())
                .toList();

        return MemberDetailRes.builder()
                .nickname(member.getNickname())
                .favorDormitoryName("세연학사")                         //  더미
                .entranceYear(member.getEntranceYear())
                .lifestyles(lifestyles)
                .profileImgUrl(urlGenerator.createCloudFrontUrl(member.getProfileImgUrl()))
                .matchCount(member.getMatchCount())
                .feedbackScore(member.getFeedbackScore())
                .memberHouse(dummyData.DummyDataForHouse())                   //  더미
                .feedbackResults(dummyData.DummyDataForMyFeedbackResults())    //  더미
                .feedbackIWrote(dummyData.DummyDataForFeedbackSession())
                .houseIOwned(dummyData.DummyDataForHouseIOwned())
                .build();
    }

    /**
     * @param memberKey:
     */
    public MemberDetailRes getDetails(UUID memberKey) {
        Member member = memberRepository.findByMemberUuid(memberKey)
                .orElseThrow(MemberNotFoundException::new);

        return MemberDetailRes.builder()
                .nickname(member.getNickname())
                .favorDormitoryName("세연학사")      //  아직 안만들어서 임시
                .entranceYear(member.getEntranceYear())
                .profileImgUrl(urlGenerator.createCloudFrontUrl(member.getProfileImgUrl()))
                .matchCount(member.getMatchCount())
                .feedbackScore(member.getFeedbackScore())
                .build();
    }

    @Transactional
    public void updateMe(long id , UpdateMemberDetailsReq updateMemberDetailsReq) {
        Member member = memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);

        List<Lifestyle> lifestyles = lifestyleService.convert(updateMemberDetailsReq.getLifestyleCodes());

        //정적 메서드 업데이트
        member.updateProfile(
                updateMemberDetailsReq.getNickname() ,
                updateMemberDetailsReq.getProfilePictureUrl()
        );

        //생활패턴 업데이트
        member.updateMemberLifestyles(lifestyles);

        //선호 기숙사 업데이트
//        member.updateDormitory(updateMemberDetailsReq.getDormitoryCode());
        member.updateDormitory("청연학사");
    }

    @Transactional
    public void deleteMember(long id , String accessToken) {
        Member member = memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);

        //BL에 추가
        securityRedisService.setValue(
                 BL_PREFIX + accessToken
                , "logout"
                , accessTokenExpire
        );

        //RefreshToken 삭제
        securityRedisService.deleteValue(RT_PREFIX + member.getEmailVerification().getEmail());

        //프로필 사진 삭제
        String profileImgUrl = member.getProfileImgUrl();

        if (!profileImgUrl.equals("common/DOTORY_DEFAULT.jpg"))
            s3Service.deleteFile(member.getProfileImgUrl());

        memberRepository.delete(member);
    }

    public void changeUserStatus(UUID memberKey , String userStatus) {
        Member member = memberRepository.findByMemberUuid(memberKey)
                .orElseThrow(MemberNotFoundException::new);

        member.changeUserStatus(UserStatus.of(userStatus));
    }


}
