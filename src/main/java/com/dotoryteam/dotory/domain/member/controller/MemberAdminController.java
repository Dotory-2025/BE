package com.dotoryteam.dotory.domain.member.controller;

import com.dotoryteam.dotory.domain.member.service.MemberService;
import com.dotoryteam.dotory.global.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/members")
public class MemberAdminController {
    private final MemberService memberService;

    @PatchMapping("/status-change/{memberKey}/{userStatus}")
    public ResponseEntity<ApiResponse<Void>> changeStatus(
            @RequestParam(name = "memberKey") UUID memberKey , @RequestParam(name = "userStatus") String userStatus) {
        memberService.changeUserStatus(memberKey , userStatus);

        return ApiResponse.ok();
    }
}
