package com.study.koreait.dto.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductPageResDto {
    private List<FindProductResDto> items; // 페이지네이션 product결과들
    private int page;
    private int size;
    private long total; // product의 총 갯수
    private int totalPage; // 계산된 페이지 갯수
    private boolean hasNext;
    private boolean hasPrev;
}
