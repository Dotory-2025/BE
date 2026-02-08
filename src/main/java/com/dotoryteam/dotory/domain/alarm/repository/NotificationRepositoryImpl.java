package com.dotoryteam.dotory.domain.alarm.repository;

import com.dotoryteam.dotory.domain.alarm.dto.response.NotificationResponse;
import com.dotoryteam.dotory.domain.alarm.dto.response.QNotificationResponse;
import com.dotoryteam.dotory.global.common.dto.CursorResult;
import com.dotoryteam.dotory.global.common.utils.CursorUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.dotoryteam.dotory.domain.alarm.entity.QNotification.notification;

@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public CursorResult<NotificationResponse> findNotificationsByCursor(Long memberId, String cursorId, int size) {
        Long cursor = CursorUtils.parseCursor(cursorId);
        List<NotificationResponse> content = queryFactory
                .select(new QNotificationResponse( // DTO 생성자 바로 호출 (QueryProjection)
                        notification.id,
                        notification.message,
                        notification.notificationType ,
                        notification.isRead ,
                        notification.createdAt
                ))
                .from(notification)
                .where(
                        notification.member.id.eq(memberId),
                        ltCursorId(cursor)
                )
                .orderBy(notification.id.desc())
                .limit(size + 1)
                .fetch();

        boolean hasNext = false;
        String nextCursorId = null;

        if (content.size() > size) {
            content.remove(size);
            hasNext = true;

            Long lastCursorId = content.get(content.size() - 1).getId();
            nextCursorId = CursorUtils.toCursor(lastCursorId);
        }

        return new CursorResult<>(content, hasNext, nextCursorId);
    }

    private BooleanExpression ltCursorId(Long cursorId) {
        if (cursorId == null) {
            return null;
        }
        return notification.id.lt(cursorId);
    }
}
