package com.dotoryteam.dotory.domain.member.repository;

import com.dotoryteam.dotory.domain.lifestyle.entity.Lifestyle;
import com.dotoryteam.dotory.domain.member.dto.response.MemberSearchRes;
import com.dotoryteam.dotory.domain.member.entity.Member;
import com.dotoryteam.dotory.global.common.dto.CursorResult;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemberRepositoryCustom {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByMemberUuid(UUID memberUuid);
    CursorResult<MemberSearchRes> searchMembers(
            UUID myMemberKey , List<String> keywords , Long minEntrance , Long maxEntrance , List<Lifestyle> lifestyle , String cursor , int size
    );
    Boolean nicknameDuplicate(String nickname);
}
