package com.dotoryteam.dotory.domain.alarm.service;

import com.dotoryteam.dotory.domain.alarm.dto.request.NotificationEvent;
import com.dotoryteam.dotory.domain.alarm.entity.FcmToken;
import com.dotoryteam.dotory.domain.alarm.entity.Notification;
import com.dotoryteam.dotory.domain.alarm.repository.FcmTokenRepository;
import com.dotoryteam.dotory.domain.alarm.repository.NotificationRepository;
import com.dotoryteam.dotory.domain.member.entity.Member;
import com.dotoryteam.dotory.domain.member.exception.MemberNotFoundException;
import com.dotoryteam.dotory.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {
    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;
    private final FcmTokenRepository fcmTokenRepository;
    private final FcmService fcmService;

    // 트랜잭션이 성공적으로 커밋했을 때만 실행됨
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async // 비동기 실행
    public void handleNotificationEvent(NotificationEvent event) {
        Member member = memberRepository.findByMemberUuid(event.getMemberUuid())
                .orElseThrow(MemberNotFoundException::new);
        // 1. DB에 알림 내역 저장
        Notification notification = Notification.builder()
                .member(member)
                .notificationType(event.getNotificationType())
                .title(event.getTitle())
                .message(event.getData() + event.getMessage())      //  사용자 명 등 추가되는 데이터가 있는 경우 getData() 에 정보 추가
                .build();

        notificationRepository.save(notification);

        // 2. FCM 푸시 발송
        List<FcmToken> tokens = fcmTokenRepository.findAllByMemberId(member.getId());
        for (FcmToken token : tokens) {
            fcmService.sendPushNotification(
                    token.getFcmToken(),
                    event.getTitle(),
                    event.getData(),
                    event.getMessage()
            );
        }
    }
}