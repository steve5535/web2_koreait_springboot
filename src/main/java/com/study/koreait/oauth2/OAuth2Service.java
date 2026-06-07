package com.study.koreait.oauth2;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

// user-info 받아오면, 정규화 해주는 서비스
@Service
public class OAuth2Service extends DefaultOAuth2UserService {

    // 인증코드를 구글로부터 받아오면, loadUser가 호출 됨
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // provider(공급자) - google or naver of daum
        String provider = userRequest.getClientRegistration().getRegistrationId();

        // 기존에 프레임워크가 사용하는 attribute를
        // 우리가 정의한 new attribute로 바꿔줄 거임
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String providerUserId = null; // provider가 사용자를 식별하는 id
        String email = null;

        switch (provider) {
            case "google":
                providerUserId = attributes.get("sub").toString();
                email = attributes.get("email").toString();
                break;
                // 네이버, 카카오 코드 최신화 필요함
            case "naver":
                Map<String, Object> naverResp = (Map<String, Object>) attributes.get("response");
                providerUserId = naverResp.get("id").toString();
                email = naverResp.get("email").toString();
                break;
            case "kakao":
                Map<String, Object> kakaoResp = (Map<String, Object>) attributes.get("kakao_account");
                providerUserId = attributes.get("id").toString();
                email = kakaoResp.get("email").toString();
                break;
        }

        // 공통형식으로 정규화
        Map<String, Object> newAttribute = Map.of(
                "providerUserId", providerUserId,
                "provider", provider,
                "email", email
        );

        // 필터에서 실행될 서비스라서, 임시 권한부여
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_TEMPORARY"));

        // DefaultOAuth2User에 담은 정보는 나중에 successHandler에서 꺼내서 사용됨
        // DefaultOAuth2User -> Authentication
        // Principal(사용자식별)값으로 뭘 쓸건지를 providerUserId로 지정해준 것
        return new DefaultOAuth2User(authorities, newAttribute, "providerUserId");
    }
}
