package com.study.koreait.mapper;

import com.study.koreait.entity.OAuth2User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface OAuth2UserMapper {
    // 계정조회(이미 소셜로그인한 기록이 있는가)
    Optional<OAuth2User> findOAuth2UserByProviderInfo(
            @Param("provider") String provider,
            @Param("providerUserId") String providerUserId
    );

    int insertOAuth2User(OAuth2User entity);

}
