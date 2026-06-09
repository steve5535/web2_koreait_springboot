package com.study.koreait.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MailException extends BusinessException {
    private final MailErrorCode errorCode;

    public MailException(MailErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode.getStatus());
        this.errorCode = errorCode;
    }
}
