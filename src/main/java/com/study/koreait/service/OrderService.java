package com.study.koreait.service;

import com.study.koreait.dto.req.PeriodPageReqDto;
import com.study.koreait.dto.res.UserOrderPageResDto.OrderResDto;
import com.study.koreait.dto.res.UserOrderPageResDto;
import com.study.koreait.entity.Orders;
import com.study.koreait.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderMapper mapper;

    @Transactional(rollbackFor = Exception.class)
    public UserOrderPageResDto getUserOrderHistory(String userId, PeriodPageReqDto dto) {
        int page = dto.getPage();
        if (page < 1) page = 1;

        int size = dto.getSize();
        if (size < 1) size = 5;
        if (size > 10) size = 10;
        int offset = (page - 1) * size;

        List<Orders> orders = mapper.findOrdersByUserId(
                userId, dto.getFromDateTime(), dto.getToDateTime(), offset, size
        );

        List<OrderResDto> items = orders.stream()
                .map(Orders::toOrderResDto)
                .toList();

        long total = mapper.countOrdersByUser(userId, dto.getFromDateTime(), dto.getToDateTime());

        int totalPages = (int) (total / size);
        if (total % size > 0) totalPages++;
        boolean hasNext = page < totalPages;
        boolean hasPrev = page > 1;

        return new UserOrderPageResDto(items, page, size, total, totalPages, hasNext, hasPrev);


    }
}
