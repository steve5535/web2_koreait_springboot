package com.study.koreait.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

// 로그인 이후에 유저가 요청을 보내면, 거치는 필터
// 우리가 직접 정의하는 커스터 jwt 필터
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException
    {
        // 1) 예비요청은 검증없이 통과시켜준다
        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            filterChain.doFilter(request, response);
        }

        // 2) 헤더에서 토큰 추출
        String authHeader = request.getHeader("Authorization");
        if (!jwtUtil.inBearer(authHeader)) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = jwtUtil.removeBearer(authHeader);

        // 3) 추출한 토큰 검증 -> 성공시 Security Context에 인증(Authentication) 저장

        try {
            Claims claims = jwtUtil.getClaims(token); // 위조, 만료 검증
            String type = claims.get("type", String.class);
            if (!"ACCESS".equals(type)) {
                throw new JwtException(("access 토큰이 아닙니다"));
            }

            // 배개변수로 넣어준 sub에 userId를 로그인시 넣어줄거임
            String userId = claims.get("sub", String.class);
            // 로그린시 extraClaims로 role도 로그인시 넣어줄거임
            String roleName = claims.get("role", String.class);

            // Authentication 객체를 만들어서 SecurityContext 저장
            // 시큐리티 권한 규칙: 접두사 "ROLE_" 필수 -> ex) ROLE_USER / ROLE_ADMIN
            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + roleName);
            List<GrantedAuthority> authorities = List.of(authority);

            JwtAuthentication authentication = new JwtAuthentication(userId, authorities);
            // 인증객체를 넣으면 이 요청은 "인증됨"으로 처리됨
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (JwtException e) {
            // 여기서 예외가 발생했을때 전파하지 않고, request에 담아둘것임
            // 뒤에 EntryPoint가 종류를 보고 알맞는 응답을 내려줄 예정
            request.setAttribute("exception", e);
        }

        filterChain.doFilter(request, response);
    }
}
