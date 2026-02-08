package com.dotoryteam.dotory.domain.alarm.repository;

import com.dotoryteam.dotory.domain.alarm.entity.FcmToken;

import java.util.List;

public interface FcmTokenRepositoryCustom {
    boolean existsByFcmToken(String FcmToken);
    List<FcmToken> findAllByMemberId(Long memberId);
}
