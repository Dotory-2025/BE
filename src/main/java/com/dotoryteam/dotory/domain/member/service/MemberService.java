package com.dotoryteam.dotory.domain.member.service;

import com.dotoryteam.dotory.domain.lifestyle.entity.Lifestyle;
import com.dotoryteam.dotory.domain.lifestyle.service.LifestyleService;
import com.dotoryteam.dotory.domain.member.dto.request.JoinReq;
import com.dotoryteam.dotory.domain.member.dto.request.UpdateMemberDetailsReq;
import com.dotoryteam.dotory.domain.member.dto.response.MemberDetailRes;
import com.dotoryteam.dotory.domain.member.dto.response.MemberSearchRes;
import com.dotoryteam.dotory.domain.member.entity.Member;
import com.dotoryteam.dotory.domain.member.exception.*;
import com.dotoryteam.dotory.domain.member.repository.MemberRepository;
import com.dotoryteam.dotory.global.common.dto.CursorResult;
import com.dotoryteam.dotory.global.redis.service.SecurityRedisService;
import com.dotoryteam.dotory.global.security.dto.JwtTokens;
import com.dotoryteam.dotory.global.security.enums.UserRole;
import com.dotoryteam.dotory.global.security.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    private final JwtUtils jwtUtils;
    private final LifestyleService lifestyleService;
    private final SecurityRedisService securityRedisService;
    private final MemberDummyData dummyData;

    private static final String AUTH_CODE_PREFIX = "SIGNUP:";
    private static final String RT_PREFIX = "RT:";

    @Value("${spring.jwt.tokens.expire.refresh_token}")
    private Long refreshTokenExpire;

    private static final String regex = "[^a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ\\u3040-\\u309F\\u30A0-\\u30FF\\u4E00-\\u9FFF\\uD800-\\uDFFF\\s]";
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
                myMemberKey , keywordParser(nicknames) , minEntrance , maxEntrance , lifestyles , cursor , size
        );

    }

    /**
     * 닉네임 중복 여부 확인 로직
     * @param nickname: 닉네임 중복 확인 시 특수문자 존재 여부 확인용 ( allowedRegex: 사용 가능한 글자 범위 - 한글 , 영어 , 일본어 , 중국어 , 이모티콘 )
     */
    public void isNicknameDuplicated(String nickname) {
        if (nickname == null || nickname.isEmpty()) {
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
     * @param memberKey: 해당 멤버의 UUID
     */
    public MemberDetailRes getMyDetail(UUID memberKey) {
        Member member = memberRepository.findByMemberUuid(memberKey)
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
                .profileImgUrl(member.getProfileImgUrl())
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
                .profileImgUrl(member.getProfileImgUrl())
                .matchCount(member.getMatchCount())
                .feedbackScore(member.getFeedbackScore())
                .build();
    }

    @Transactional
    public void updateMe(UUID memberKey , UpdateMemberDetailsReq updateMemberDetailsReq) {
        Member member = memberRepository.findByMemberUuid(memberKey)
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


    /**
     * 사용자 검색 용 키워드 파서 ( 사용자 닉네임은 특수문자가 불가능하므로 서버에서 global 이중검증 로직으로 사용되긴 애매함 )
     * @param keywords: 검색창에 입력한 전체 값들을 받아옴
     * @return: 사용 불가능한 특수문자들 , 불필요한 띄어쓰기 들을 제거 후 List 로 반환
     *
     * 여러 명 찾는 경우
     *     특수 문자 자체가 안되므로 프론트 , 뱍앤드 이중검증
     *     한국어 , 영어 , 일본어 , 중국어 가능하도록
     *     닉네임 중복 시 오류 던지는 로직이랑 같이 해보려 했는데 둘의 성격이 너무 다름
     *     닉네임 중복은 null 이 올 수 없고 특수 문자가 들어가면 오류를 던져야 함 (InvalidNicknameFormatException)
     *     parser 는 검색한 키워드 (여러 인물을 검색하는 경우) 를 list 로 반환하는 로직이 필요, 그 과정에서 받은 특수문자들은 치환해버려야함
     */
    private List<String> keywordParser(String keywords) {
        if (keywords == null || keywords.trim().isEmpty()) {
            return new ArrayList<>();
        }

        //영어 , 힌글 , 일본어 , 중국어 정도만 처리 , 일본어는 히라가나 가타카나 모두 가능하도록
        //이모티콘 가능(uD0800 - uDFFF) , 특수 문자 기호는 불가능
        // 허용되지 않는 특수문자를 공백으로 치환해버리는게 더 나음
        //애초에 띄어쓰기가 포함되어야 하는 키워드도 그냥 분리해서 검색하는 게 낫다.
        String cleanKeywords = keywords.replaceAll(regex, " ");

        //공백 기준으로 자르고, 빈 문자열 제거 후 리스트로 반환
        return Arrays.stream(cleanKeywords.split("\\s+")) // 공백 여러 개도 하나로 처리하도록
                .filter(keyword -> !keyword.isBlank())
                .collect(Collectors.toList());
    }
}
