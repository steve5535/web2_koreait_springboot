package com.study.koreait.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

// enum으로 응답상태코드 + 메세지
@Getter
public enum MailErrorCode {
    USER_NOT_FOUNT(HttpStatus.NOT_FOUND, "사용자 정보를 찾을 수 없습니다"),
    ALREADY_VERIFIED(HttpStatus.CONFLICT, "이미 인증이 완료된 이메일 입니다"),
    SEND_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "메일 발송에 실패했습니다")
    ;

    private final HttpStatus status;
    private final String message;

    MailErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
