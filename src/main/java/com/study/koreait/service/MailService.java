package com.study.koreait.service;

import com.study.koreait.entity.MailAuditLog;
import com.study.koreait.entity.Users;
import com.study.koreait.exception.MailErrorCode;
import com.study.koreait.exception.MailException;
import com.study.koreait.jwt.JwtUtil;
import com.study.koreait.mapper.MailLogMapper;
import com.study.koreait.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

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
            recordLog(userId, null, "SEND", "FAILED", MailErrorCode.USER_NOT_FOUNT.getMessage());
            throw new MailException(MailErrorCode.USER_NOT_FOUNT);
        }
        // 이미 인증했는지?
        if (user.getEmailVerified()) {
            recordLog(userId, user.getEmail(), "SEND", "FAILED", MailErrorCode.USER_NOT_FOUNT.getMessage());
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

    public Map<String, Object> verify(String token) {

        return Map.of(
                "status", "success",
                "message", "이메일 인증이 완료되었습니다"
        );
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
