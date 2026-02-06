package com.dotoryteam.dotory.domain.member.utils;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class NicknameKeywordParser {
    private static final String regex = "[^a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ\\u3040-\\u309F\\u30A0-\\u30FF\\u4E00-\\u9FFF\\uD800-\\uDFFF\\s]";

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
    public List<String> keywordParser(String keywords) {
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
