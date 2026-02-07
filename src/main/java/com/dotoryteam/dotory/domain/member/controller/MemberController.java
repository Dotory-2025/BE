package com.dotoryteam.dotory.domain.member.controller;

import com.dotoryteam.dotory.domain.member.dto.request.JoinReq;
import com.dotoryteam.dotory.domain.member.dto.request.UpdateMemberDetailsReq;
import com.dotoryteam.dotory.domain.member.dto.response.MemberDetailRes;
import com.dotoryteam.dotory.domain.member.dto.response.MemberSearchRes;
import com.dotoryteam.dotory.domain.member.repository.MemberRepository;
import com.dotoryteam.dotory.domain.member.service.MemberService;
import com.dotoryteam.dotory.global.common.dto.ApiResponse;
import com.dotoryteam.dotory.global.common.dto.CursorResult;
import com.dotoryteam.dotory.global.security.dto.JwtTokens;
import com.dotoryteam.dotory.global.security.utils.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity<ApiResponse<JwtTokens>> join(@RequestBody JoinReq joinReq) {
        JwtTokens tokens = memberService.join(joinReq);

        return ApiResponse.ofToken(tokens);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<CursorResult<MemberSearchRes>>> search(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(required = false) String keyword ,
            @RequestParam(required = false) Long minEntrance ,
            @RequestParam(required = false) Long maxEntrance ,
            @RequestParam(required = false) List<String> lifestyle ,
            @RequestParam(required = false) String cursor ,
            @RequestParam(defaultValue = "20") int size
    ) {
        CursorResult<MemberSearchRes> searchResult = memberService.search(
                userPrincipal.getMemberKey() , keyword , minEntrance , maxEntrance , lifestyle , cursor , size
        );

        return ApiResponse.ok(searchResult);
    }

    @GetMapping("/nickname-duplicate")
    public ResponseEntity<ApiResponse<Void>> nicknameDuplicate(@RequestParam String nickname) {
        memberService.isNicknameDuplicated(nickname);

        return ApiResponse.ok();
    }

    @GetMapping("/{memberKey}")
    public ResponseEntity<ApiResponse<MemberDetailRes>> getMemberDetail(@PathVariable UUID memberKey) {
        return ApiResponse.ok(memberService.getDetails(memberKey));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MemberDetailRes>> getMyDetails(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ApiResponse.ok(memberService.getMyDetail(userPrincipal.getId()));
    }

    @PatchMapping("/me")
    public ResponseEntity<ApiResponse<Void>> changeMemberDetails(
            @AuthenticationPrincipal UserPrincipal userPrincipal ,
            @RequestBody UpdateMemberDetailsReq updateMemberDetailsReq
            ) {
        memberService.updateMe(userPrincipal.getId() , updateMemberDetailsReq);
        return ApiResponse.ok();
    }

    @DeleteMapping("/withdraw")
    public ResponseEntity<ApiResponse<Void>> withdraw(
            @AuthenticationPrincipal UserPrincipal userPrincipal ,
            HttpServletRequest request
    ) {
        memberService.deleteMember(userPrincipal.getId() , request.getHeader("Authorization"));
        return ApiResponse.ok();
    }
}
