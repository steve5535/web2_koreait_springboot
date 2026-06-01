package com.study.koreait.service;

import com.study.koreait.dto.req.AddProductReqDto;
import com.study.koreait.dto.req.ModifyProductReqDto;
import com.study.koreait.dto.req.PageReqDto;
import com.study.koreait.dto.req.SearchProductReqDto;
import com.study.koreait.dto.res.FindProductResDto;
import com.study.koreait.dto.res.ProductPageResDto;
import com.study.koreait.entity.Product;
import com.study.koreait.mapper.ProductMapper;
import com.study.koreait.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    // 인터페이스 타입을 필드로 가진다
    // -> 의존성이 느슨하기 때문에 변경이 쉽다
    private final ProductRepository repository;
    private final ProductMapper mapper;

    // 전체 상품리스트를 리턴 - dto
    public List<FindProductResDto> getProductList() {
        return mapper.findAllProducts()
                .stream()
                // 매개변수가 호출만 될때 or 다음메서드에 전달만 될때
                // 메서드참조라는 람다 생략식을 작성할 수 있음
                .map(Product::toFindProductResDto)
                .toList();
    }

    // 특정 상품을 리턴 - dto
    // 메서드마다 dto를 작성해줘야함
    public FindProductResDto getProductById(int id) {
        return mapper.findProductById(id).toFindProductResDto();
    }

    public int addProduct(AddProductReqDto dto) {
        return mapper.insertProduct(dto.toEntity());
    }

    public int removeProduct(int id) {
        return mapper.deleteProductById(id);
    }

    public int modifyProduct(int id, ModifyProductReqDto dto) {
        return repository.updateProductById(dto.toEntity(id));
    }

    // dto는 공유라는 용도가 아님
    // 권장은 메서드별로 하나씩 새로 만들어서
    public List<FindProductResDto> getSearchProducts(String name) {
        return mapper.searchProductByName(name).stream()
                .map(Product::toFindProductResDto)
                .toList();
    }

    // 원래는 따로 dto 정의해서 사용해야함
    public List<FindProductResDto> dynamicSearchProduct(SearchProductReqDto dto) {
        return mapper.detailSearchProduct(dto.getNameKeyword(), dto.getMinPrice(), dto.getMaxPrice())
                .stream()
                .map(Product::toFindProductResDto)
                .toList();
    }

    public List<FindProductResDto> prioritySearchProduct(SearchProductReqDto dto) {
        return mapper.chooseSearchProduct(dto.getMinPrice(), dto.getMaxPrice())
                .stream()
                .map(Product::toFindProductResDto)
                .toList();
    }

    public int addBulkProducts(List<AddProductReqDto> dtos) {
        List<Product> products = dtos.stream()
                .map(AddProductReqDto::toEntity)
                .toList();
        return mapper.insertProducts(products);
    }

    // 쿼리 두개가 하나의 작업(트랜잭션)으로 묶는다.
    // 안 묶으면, 두 쿼리 사이에 다른 트랜잭션이 insert/delete 해서 items와 total이
    // 안 맞을 수 있음.
    // 예외가 발생하면 롤백
    // 기본값은 RuntimeException 발생시 rollback
    @Transactional(rollbackFor = Exception.class)
    public ProductPageResDto getProductPage(PageReqDto dto) {
        // 1) 입력값 보정
        int page = dto.getPage();
        if (page < 1) page = 1;

        int size = dto.getSize();
        if (size < 1) size = 10;
        if (size > 50) size = 50; // 너무 크면 서버 부담

        // 2) 한페이지 분량 조회
        int offset = (page - 1) * size;
        List<FindProductResDto> items = mapper.findPage(offset, size)
                .stream()
                .map(Product::toFindProductResDto)
                .toList();

        // 3) 전체 갯수
        long totalProductCount = mapper.countAll();
        int totalPages = (int) (totalProductCount / size);
        // 나머지가 0보다 크다면 page + 1
        if (totalPages % size > 0) totalPages++;

        // 사용자가 요청한 페이지가 최대페이지보다 작으면 다음페이지 있음
        boolean hasNext = page <totalPages;
        boolean hasPrev = page > 1;

        return new ProductPageResDto(items, page, size, totalProductCount, totalPages, hasNext, hasPrev);
    }

}
