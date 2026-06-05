package com.study.koreait.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

/*
    jwt 토큰 발급 & 검증용 유틸클래스

 */
@Component
public class JwtUtil {
    private final SecretKey key;
    private final long accessExpireMillis;

    public JwtUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-expire-millis}") long accessExpireMillis
    ) {
        this.accessExpireMillis = accessExpireMillis;
        // secret(base64 문자열) -> 바이트 -> HS256키
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    // 발급관련 메서드 //
    public String generateAccessToken(String subject, Map<String, Object> extraClaims) {
        long now = System.currentTimeMillis();

        JwtBuilder builder = Jwts.builder()
                .subject(subject) // 유저식별
                .issuedAt(new Date(now)) // 발급시각
                .expiration(new Date(now + accessExpireMillis)) // 만료시각
                .claim("type", "ACCESS"); // 토큰타입

        if (extraClaims != null) {
            extraClaims.forEach(builder::claim);
        }

        return builder.signWith(key).compact();
    }

    // 검증관련 메서드 //
    // (1) 위조 검사
    // (2) 만료시간 검사
    // (3) claim들 가져오기
    public Claims getClaims(String token) throws JwtException {
        return Jwts.parser()
                .verifyWith(key) // 위조검사 & 만료시간 검사
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // 요청 헤더가 "Bearer <토큰>" 형식인지 확인, 제거 메서드
    public boolean inBearer(String header) {
        boolean isNotNull = header != null;
        boolean isStartWithBearer = header.startsWith("Bearer ");
        return isNotNull && isStartWithBearer;
    }

    // Bearer ekslfjsefl"
    public String removeBearer(String header) {
        return header.substring("Bearer ".length());
    }



}
