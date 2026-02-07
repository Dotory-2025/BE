package com.dotoryteam.dotory.domain.alarm.service;

import com.dotoryteam.dotory.domain.alarm.exception.FcmMessageSendException;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;

@Service
@RequiredArgsConstructor
public class FcmService {
    @Async
    public void sendPushNotification(String fcmToken , String title ,  @Nullable String data , String body) {
        if (fcmToken == null || fcmToken.isEmpty()) return;
        try {
            Notification googleNotification = Notification.builder()
                    .setTitle(title)
                    .setBody(data + body)
                    .build();

            Message message = Message.builder()
                    .setToken(fcmToken)
                    .setNotification(googleNotification)
                    .build();

            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            throw new FcmMessageSendException();
        }
    }
}