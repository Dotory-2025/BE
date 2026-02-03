package com.dotoryteam.dotory.global.security.utils;

import com.dotoryteam.dotory.domain.member.entity.Member;
import com.dotoryteam.dotory.domain.member.enums.Gender;
import com.dotoryteam.dotory.domain.member.repository.MemberRepository;
import com.dotoryteam.dotory.global.security.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {
    private final MemberRepository memberRepository;

    @Override
    public void run(String... args) throws Exception {
        if (memberRepository.findByEmail("dotoryinfo@dotory-app.kr").isPresent()) return;

        Member admin = Member.builder()
                .nickname("admin")
                .sex(Gender.M)
                .email("dotoryinfo@dotory-app.kr")
                .userRole(UserRole.ADMIN)
                .profileImgUrl("")
                .build();

        // 3. DB 저장
        memberRepository.save(admin);
    }
}