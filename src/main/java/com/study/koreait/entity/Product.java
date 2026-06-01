package com.study.koreait.entity;

import com.study.koreait.dto.res.FindProductResDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// entity
// db의 컬럼명과 필드명이 1:1 매칭된 자바클래스
@AllArgsConstructor
@NoArgsConstructor
@Data @Builder
public class Product {
    private int productId;
    private String productName;
    private int price;
    private LocalDateTime createAt;

    // dto변환을 entity가 가질 수 있음
    // 단, dto변경시 entity까지 영향을 받을 수 있다.
    public FindProductResDto toFindProductResDto() {
        return FindProductResDto.builder()
                .productName(this.productName)
                .price(this.price)
                .build();
    }
}
