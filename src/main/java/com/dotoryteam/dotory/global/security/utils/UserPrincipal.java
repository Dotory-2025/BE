package com.dotoryteam.dotory.global.security.utils;

import com.dotoryteam.dotory.domain.member.entity.Member;
import com.dotoryteam.dotory.global.security.enums.UserRole;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class UserPrincipal implements UserDetails {

    // 우리가 만든 진짜 유저 정보 (여기에 id, email, uuid 다 들어있음)
    private final Member member;

    // [핵심] 컨트롤러에서 user.getId()로 편하게 쓰기 위해 만든 메서드
    public Long getId() {
        return member.getId();
    }

    public UUID getMemberUuid() {
        return member.getMemberKey(); // UUID 필요할 때 쓰려고 추가
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(UserRole.ROLE_USER.toString()));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return member.getEmailVerification().getEmail(); // 보통 식별자로 이메일을 씀
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
