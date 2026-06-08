package com.study.koreait.oauth2;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    /*
        서비스에서 정규화 해둔 DefaultOAuth2User를 꺼내서
        우리 서버의 엔드포인트(컨트롤러)로 리다이렉트
     */

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
        String provider = oAuth2User.getAttribute("provider");
        String providerUserId = oAuth2User.getAttribute("providerUserId");
        String email = oAuth2User.getAttribute("email");

        // 우리 엔트포인트(컨트롤러)로 get요청
        String redirectURL = "/auth/oauth2"
                + "?provider=" + provider
                + "&providerUserId=" + providerUserId
                + "&email=" + URLEncoder.encode(email == null ? "" : email, StandardCharsets.UTF_8);

        try {
            response.sendRedirect(redirectURL);
        } catch (IOException e) {
            log.error("OAuth2 성공 후 리다이렉트 실패 - IO", e);
        } catch (Exception e) {
            log.error("OAuth2 성공 후 리다이렉트 실패 - 기타", e);
        }


    }
}
