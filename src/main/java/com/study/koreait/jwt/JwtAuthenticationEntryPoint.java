package com.study.koreait.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

// 인증 실패시 처리하는 곳
// 1) 토큰 검증 실패시
// 2) 필터가 끝날때까지 authentication이 없는 경우
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    // 3가지 경우의 수의 에러 메세지들을 상수로 정의
    public static final String EXPIRED_ERROR_MSG = """
            {"error": "ACCESS_TOKEN_EXPIRED"}
            """;
    public static final String INVALID_ERROR_MSG = """
            {"error": "INVALID_ACCESS_TOKEN"}
            """;
    public static final String UNAUTHORIZED_MSG = """
            {"error": "UNAUTHORIZED"}
            """;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        // 필터에서 우리가 담아줬던 예외객체를 꺼낸다
        Exception e = (Exception) request.getAttribute("exception");

        // 컨트롤러가 아닌 여기서 바로 응답
        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401

        // 만료는 따로 분기(프론트에서 재로그인 유도 등 구별하기 좋도록)
        if (e instanceof ExpiredJwtException) {
            response.getWriter().write(EXPIRED_ERROR_MSG);
            return;
        }
        // 그외 JWT 예외(위조 등..)
        if (e instanceof JwtException) {
            response.getWriter().write(INVALID_ERROR_MSG);
            return;
        }

        // 토큰 자체가 없는 경우 등..
        response.getWriter().write(UNAUTHORIZED_MSG);

    }
}
