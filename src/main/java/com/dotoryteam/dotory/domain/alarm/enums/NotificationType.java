package com.dotoryteam.dotory.domain.alarm.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {
    HOUSE_NEW_MEMBER("새로운 멤버가 하우스에 참여했어요.") ,
    HOUSE_MEMBER_WITHDRAW("님이 하우스를 나갔어요.") ,
    HOUSE_DELETED("방장이 하우스를 삭제했어요.") ,
    HOUSE_TITLE_MODIFY("하우스 제목이 변경되었어요") ,
    HOUSE_RULE_MODIFY("하우스 규칙이 변경되었어요") ,
    HOUSE_DESCRIPTION_MODIFY("하우스 소개글이 변경되었어요") ,
    HOUSE_WITHDRAWN_BY_OWNER("방장에 의해 추방당했어요.");

    private final String notificationMessage;
}
