package com.study.koreait.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
// 유저별 구매내역 페이지 응답 dto
public class UserOrderPageResDto {
    private List<OrderResDto> items;
    private int page;
    private int size;
    private long total;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrev;

    // 내부 정적 클래스 -> 사실상 별도의 클래스
    // 중첩되어서 표현되니까 관련 dto가 한 파일에 모여있게 할 수 있음
    // 구매 건 하나에 대한 dto
    @AllArgsConstructor
    @NoArgsConstructor
    @Data @Builder
    public static class OrderResDto {
        private int orderId;
        private LocalDateTime orderedAt;
        private List<OrderDetailResDto> orderDetails;
    }


    // 구매내역의 상세 한줄 dto
    @AllArgsConstructor
    @NoArgsConstructor
    @Data @Builder
    public static class OrderDetailResDto {
        private String productName;
        private int price;
        private int quantity;
    }

}
