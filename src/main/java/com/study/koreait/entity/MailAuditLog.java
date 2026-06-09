package com.study.koreait.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// 감사용 테이블 엔티티
@AllArgsConstructor
@NoArgsConstructor
@Data @Builder
public class MailAuditLog {
    private int logId;
    private String userId;
    private String email;
    private String eventType;
    private String result;
    private String message;
    private LocalDateTime createAt;
}
