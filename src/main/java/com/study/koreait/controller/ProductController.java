package com.study.koreait.controller;

import com.study.koreait.dto.AddProductReqDto;
import com.study.koreait.dto.ModifyProductReqDto;
import com.study.koreait.dto.SearchProductReqDto;
import com.study.koreait.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;

    @GetMapping("/all")
    public ResponseEntity<?> getProducts() {
        return ResponseEntity.ok(service.getProductList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable int id) {
        return ResponseEntity.ok(service.getProductById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<?> postProduct(@RequestBody AddProductReqDto dto) {
        int successCount = service.addProduct(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(successCount + "건 등록 성공");
    }

    // Delete요청은 body가 존재하지만, 잘 사용하지 않는다.
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable int id) {
        int successCount = service.removeProduct(id);
        return ResponseEntity.ok(successCount + "건 삭제 성공");
    }

    // RESTful -> id는 path로 지정,
    // 구체적인 내용은 body
    @PutMapping("/{id}")
    public ResponseEntity<?> putProduct(@PathVariable int id, @RequestBody ModifyProductReqDto dto) {
        int successCount = service.modifyProduct(id, dto);
        return ResponseEntity.ok(successCount + "건 수정완료");
    }

    // localhost:8080/product/search?name=마우스
    @GetMapping("/search")
    public ResponseEntity<?> getProductByName(@RequestParam("name") String name) {
        return ResponseEntity.ok(service.getSearchProducts(name));
    }

    @GetMapping("/search/dynamic")
    public ResponseEntity<?> searchProductsDynamic(SearchProductReqDto dto) {
        return ResponseEntity.ok(service.dynamicSearchProduct(dto));
    }

    @GetMapping("/search/priority")
    public ResponseEntity<?> searchProductPriority(SearchProductReqDto dto) {
        return ResponseEntity.ok(service.prioritySearchProduct(dto));
    }

    @PostMapping("add/bulk")
    public ResponseEntity<?> addProducts(@RequestBody List<AddProductReqDto> dots) {
        int successCount = service.addBulkProducts(dots);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(successCount + "건 등록 성공");
    }

}
