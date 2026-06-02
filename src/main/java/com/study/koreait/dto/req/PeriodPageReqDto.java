package com.study.koreait.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PeriodPageReqDto {
    private LocalDate from; // 조회 시작일 (포함)
    private LocalDate to; // 조회 종료일 (제외)
    private int page = 1;
    private int size = 5;

    // LocalDate -> 일자만 표현
    // LocalDateTime -> 초까지 표현

    public LocalDateTime getFromDateTime() {
        // atStartOfDay: 2026-06-01 -> 2026-06-01 00:00:00
        return from == null ? null : from.atStartOfDay();
    }

    public LocalDateTime getToDateTime() {
        // 2026-06-02 -> 2026-06-03 00:00:00
        // 미만으로 잡기때문에 바로 직전일자 자정 11시 59분 59초 까지
        return to == null ? null : to.plusDays(1).atStartOfDay();
    }


}
