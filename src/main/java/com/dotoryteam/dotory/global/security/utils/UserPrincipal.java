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

    public Long getId() {
        return member.getId();
    }

    public UUID getMemberKey() {
        return member.getMemberKey(); // UUID 필요할 때 쓰려고 추가
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (member.getUserRole() == null)
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + UserRole.USER));
        else {
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + member.getUserRole()));
        }
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
