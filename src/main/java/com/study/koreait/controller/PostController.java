package com.study.koreait.controller;

import com.study.koreait.dto.AddPostReqDto;
import com.study.koreait.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService service;

    @GetMapping("/all")
    public ResponseEntity<?> getAllPost() {
        return ResponseEntity.ok(service.getAllPost());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable int id) {
        return ResponseEntity.ok(service.getPostById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addPost(@RequestBody @Valid AddPostReqDto dto) {
        int successCount = service.addPost(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(successCount + "건 추가완료");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable int id) {
        int successCount = service.removePost(id);
        return ResponseEntity.ok(successCount + "건 삭제 완료");
    }
}
