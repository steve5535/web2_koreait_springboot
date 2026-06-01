package com.study.koreait.mapper;

import com.study.koreait.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper {
    // 기간별 페이지네이션된 유저별 구매내역
    List<Orders> findOrdersByUserId(
            @Param("userId") String userId,
            @Param("from")LocalDateTime from,
            @Param("to") LocalDateTime to,
            @Param("offset") int offset,
            @Param("size") int size
    );

    // 유저가 해당 기간에 주문한 건수
    long countOrdersByUser(
            @Param("userId") String userId,
            @Param("from")LocalDateTime from,
            @Param("to") LocalDateTime to
    );

}
