package com.study.koreait.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// user : OAuth2User <-> 1 : N
@AllArgsConstructor
@NoArgsConstructor
@Data @Builder
public class OAuth2User {
    private int oauth2UserId; // 테이블 식별자 pk
    private String userId; // users 테이블의 fk
    private String provider; // 구글/카카오/네이버
    private String providerUserId; // 구글/카카오/네이버가 알려주는 식별자
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
