package com.dotoryteam.dotory.domain.alarm.controller;

import com.dotoryteam.dotory.domain.alarm.dto.response.NotificationResponse;
import com.dotoryteam.dotory.domain.alarm.service.NotificationService;
import com.dotoryteam.dotory.global.common.dto.ApiResponse;
import com.dotoryteam.dotory.global.common.dto.CursorResult;
import com.dotoryteam.dotory.global.security.utils.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/")
    public ResponseEntity<ApiResponse<CursorResult<NotificationResponse>>> getNotifications(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam String cursor ,
            @RequestParam Integer size) {
        return ApiResponse.ok(notificationService.getMyNotifications(userPrincipal.getId() , cursor , size));
    }

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<ApiResponse<CursorResult<Void>>> readNotifications(
            @PathVariable Long notificationId ,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        notificationService.markAsRead(notificationId , userPrincipal.getId());
        return ApiResponse.ok();
    }
}
