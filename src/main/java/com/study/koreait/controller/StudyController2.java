package com.study.koreait.controller;

import com.study.koreait.dto.req.AddPostReqDto;
import com.study.koreait.dto.req.AddProductReqDto;
import com.study.koreait.dto.res.ProductResDto;
import com.study.koreait.exception.ProductException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/study2")
public class StudyController2 {
    @GetMapping("/add")
    public Integer add(Integer num1, Integer num2) {
        log.info("들어온 데이터: {}, {}", num1, num2);
        Integer add = num1 + num2;
        return add;
    }

    @GetMapping("/avg")
    public double avg(Integer num1, Integer num2, Integer num3) {
        log.info("들어온 데이터: {}, {}, {}", num1, num2, num3);
        double avg = (num1 + num2 + num3) / 3.0;
        return avg;
    }

    // 실습) id를 받아서 해당 id의 상품을 Map으로 리턴
    // id가 없는 경우 처리하는 코드
    @GetMapping("/product/{id}")
    public ResponseEntity<?> getProductBuId(@PathVariable int id) {
        List<ProductResDto> dtos = List.of(
                new ProductResDto(1, "아이패드", 879000),
                new ProductResDto(2, "로지텍 마우스", 39000),
                new ProductResDto(3, "무접점 키보드", 119000)
        );

        Optional<ProductResDto> optDto = dtos.stream()
                .filter(p -> p.getId() == id)
                .findFirst();

        // 옵셔널.orElseThrow()
        // 커스텀 에러를 던져줘야함.
        ProductResDto dto = optDto
                .orElseThrow(() -> new ProductException("해당 id는 존재하지 않습니다", HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(dto);
    }

    // 실습2) 쿼리스트링으로 minPrice, maxPrice 받아서 범위에 해당하는 상품리스트 리턴
    @GetMapping("/products/range")
    public Map<String, Object> getProductsBuPriceRange(@RequestParam int minPrice, @RequestParam int maxPrice) {
        List<ProductResDto> dtos = List.of(
                new ProductResDto(1, "아이패드", 879000),
                new ProductResDto(2, "로지텍 마우스", 39000),
                new ProductResDto(3, "무접점 키보드", 119000)
        );

        List<ProductResDto> datas = dtos.stream()
                .filter(p -> p.getPrice() >= minPrice && p.getPrice() <= maxPrice)
                .toList();


        // 범위안에 없으면 빈 리스트 리턴
        return Map.of("success", datas);
    }

    // post(만들겠다) + /post -> 게시글을 등록하겠다
    // post요청 + /posts -> 게시글 여러개을 등록하겠다
    // (요청메서드 + 요청url)는 중복 x
    @PostMapping("/post")
    public ResponseEntity<?> makePost(@RequestBody @Valid AddPostReqDto dto) {
        log.info("들어온 데이터: {}", dto);

        return ResponseEntity //
                .status(HttpStatus.CREATED)
                .body("성공");
    }

    // PUT: 수정 - 덮어쓰기
    // PATCH: 수정 - 부분수정

    // DELETE는 body가 있으나, path로 지정
    @DeleteMapping("/post/{id}")
    public ResponseEntity<?> deletePost(@PathVariable int id) {
        return ResponseEntity.ok("성공");
    }

    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestBody @Valid AddProductReqDto dto) {
        // 상품등록
        return ResponseEntity.ok("상품등록 완료");
    }

}
