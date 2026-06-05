package com.study.koreait.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

// jwt 인증성공시 만들어진 Authentication 객체
@RequiredArgsConstructor
public class JwtAuthentication implements Authentication {
    private final String userId; // sub(유저 식별자)
    private final List<GrantedAuthority> authorities; // 권한(여러개 있을 수 있어서 list
    private boolean isAuthenticated = true; // 인증여부

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return null; // 세션용
    }

    @Override
    public Object getDetails() {
        return null; // 세션용
    }

    @Override
    public Object getPrincipal() {
        return userId; // 시큐리티에서 사용자 식별자를 principal이라고 부름
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        // 인증 취소 가능성을 고려하여 기능을 만들어 둠
        this.isAuthenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return userId; // 인증 식별자 = 사용자 식별자
    }
}
