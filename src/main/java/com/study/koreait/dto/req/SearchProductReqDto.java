package com.study.koreait.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SearchProductReqDto {
    // 세 필드 모두 있을수도있고 없을수도 있음
    // 없을때는 null로 통일하기 위해 래퍼클래스 사용
    private String nameKeyword;
    private Integer minPrice;
    private Integer maxPrice;
}
