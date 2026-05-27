package com.study.koreait.service;

import com.study.koreait.dto.AddProductReqDto;
import com.study.koreait.dto.FindProductResDto;
import com.study.koreait.dto.ModifyProductReqDto;
import com.study.koreait.entity.Product;
import com.study.koreait.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    // 인터페이스 타입을 필드로 가진다
    // -> 의존성이 느슨하기 때문에 변경이 쉽다
    private final ProductRepository repository;

    // 전체 상품리스트를 리턴 - dto
    public List<FindProductResDto> getProductList() {
        return repository.findAllProducts()
                .stream()
                // 매개변수가 호출만 될때 or 다음메서드에 전달만 될때
                // 메서드참조라는 람다 생략식을 작성할 수 있음
                .map(Product::toFindProductResDto)
                .toList();
    }

    // 특정 상품을 리턴 - dto
    // 메서드마다 dto를 작성해줘야함
    public FindProductResDto getProductById(int id) {
        return repository.findProductById(id).toFindProductResDto();
    }

    public int addProduct(AddProductReqDto dto) {
        return repository.insertProduct(dto.toEntity());
    }

    public int removeProduct(int id) {
        return repository.deleteProductById(id);
    }

    public int modifyProduct(int id, ModifyProductReqDto dto) {
        return repository.updateProductById(dto.toEntity(id));
    }

    // dto는 공유라는 용도가 아님
    // 권장은 메서드별로 하나씩 새로 만들어서
    public List<FindProductResDto> getSearchProducts(String name) {
        return repository.searchProductByName(name).stream()
                .map(Product::toFindProductResDto)
                .toList();
    }

}
