package com.study.koreait.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
// 페이지네이션 전용 요청 dto
public class PageReqDto {
    // 쿼리스트링으로 데이터가 안들어와도,
    // 기본값 지정
    private int page = 1;
    private int size = 10;
}
