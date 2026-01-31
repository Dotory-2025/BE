package com.dotoryteam.dotory.domain.lifestyle.controller;

import com.dotoryteam.dotory.domain.lifestyle.repository.MemberLifestyleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lifestyle")
public class MemberLifestyleController {
    private final MemberLifestyleRepository memberLifestyleRepository;

    @GetMapping("/{memberKey}")
    public List<String> memberLifestyle(@PathVariable UUID memberKey) {
        return memberLifestyleRepository.findKeywordsByMemberKey(memberKey);
    }
}
