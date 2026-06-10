package com.study.koreait.service;

import com.study.koreait.entity.MailAuditLog;
import com.study.koreait.entity.Users;
import com.study.koreait.exception.MailErrorCode;
import com.study.koreait.exception.MailException;
import com.study.koreait.jwt.JwtUtil;
import com.study.koreait.mapper.MailLogMapper;
import com.study.koreait.mapper.UserMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class MailService {
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final JavaMailSender javaMailSender; // 메일발송 자바객체
    private final MailLogMapper mailLogMapper;// 감사로그 기록매퍼

    public Map<String, String> sendVerifyMail(String userId) {
        // 회원(유저)이 맞는지 확인
        Users user = userMapper.getUserById(userId).orElse(null);
        if (user == null) {
            recordLog(userId, null, "SEND", "FAILED", MailErrorCode.USER_NOT_FOUND.getMessage());
            throw new MailException(MailErrorCode.USER_NOT_FOUND);
        }
        // 이미 인증했는지?
        if (Boolean.TRUE.equals(user.getEmailVerified())) {
            recordLog(userId, user.getEmail(), "SEND", "FAILED", MailErrorCode.ALREADY_VERIFIED.getMessage());
            throw new MailException(MailErrorCode.ALREADY_VERIFIED);
        }
        // jwt 토큰을 만든다 - 인증링크 유효검사용
        String token = jwtUtil.generateEmailVerifyToken(user.getUserId());
        // url 조립 - 우리 컨트롤러(아래의 인증하기 버튼을 눌러주세요 하면 연결된 url)
        String verifyUrl = "http://localhost:8080/mail/verify?token=" + token;

        // 메일 구성
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail()); // 메일 도착경우
        message.setSubject("[스프링부트 인증] 이메일 인증을 완료하세요"); // 메일제목
        message.setText("아래의 링크를 클랙해 이메일 인증을 완료해 주세요. \n" + verifyUrl); // 메일 본문

        // 발송
        // 외부서버에 의존하기 때문에 예외 처리 해줌
        try {
            javaMailSender.send(message);
        } catch (RuntimeException e) {
            recordLog(userId, user.getEmail(), "SEND", "FAILED", MailErrorCode.SEND_FAIL.getMessage());
            throw new MailException(MailErrorCode.SEND_FAIL);
        }

        // 여기가 실행되면 발송 성공
        recordLog(userId, user.getEmail(), "SEND", "SUCCESS", "인증 메일 전송 완료");
        return Map.of("success", "인증 메일이 전송되었습니다");
    }

    // 검증된 이메일 토큰을 받아왔는가
    // 정상적인 경로(이메일에 있는 링크)가 아니라 훔쳐서 들어온 경우
    // 만료시간 이후에 눌렀는지
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> verify(String token) {
        try {
            Claims claims = jwtUtil.getClaims(token);
            if (!claims.get("type", String.class).equals("EMAIL_VERIFY")) {
                recordLog(claims.getSubject(), null, "VERIFY", "FAILED", "잘못된 인증 요청");
                return Map.of("status", "failed", "message", "잘못된 인증 요청");
            }

            String userId = claims.getSubject();
            Users user = userMapper.getUserById(userId).orElse(null);
            if (user == null) {
                recordLog(claims.getSubject(),null, "VERIFY", "FAILED", "사용자 찾을 수 없음");
                return Map.of("status", "failed", "message", "사용자 찾을 수 없음");
            }

            // users 테이블 업데이트
            // 실습) email_verified가 true로(or 1), email_verified_at이 업데이트시간으로
            userMapper.updateEmailVerified(userId);
            recordLog(userId, user.getEmail(), "VERIFY", "SUCCESS", "이메일 인증이 완료되었습니다");

            return Map.of(
                    "status", "success",
                    "message", "이메일 인증이 완료되었습니다"
            );
        } catch (ExpiredJwtException e) {
            recordLog(null,null, "VERIFY", "FAILED", "만료된 인증요청");
            return Map.of("status", "failed", "message", "만료된 인증요청. 다시 요청하세요");
        } catch (JwtException e) {
            recordLog(null, null, "VERIFY", "FAILED", "유요하지 않은 인증");
            return Map.of("status", "failed", "message", "유요하지 않은 인증");
        }


    }

    private void recordLog(String userId, String email, String eType, String result, String message) {
        MailAuditLog log = MailAuditLog.builder()
                .userId(userId)
                .email(email)
                .eventType(eType)
                .result(result)
                .message(message)
                .build();
        mailLogMapper.addLog(log);
    }
}
