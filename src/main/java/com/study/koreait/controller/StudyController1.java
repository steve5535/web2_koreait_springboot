package com.study.koreait.controller;

import com.study.koreait.dto.res.StudentResDto;
import com.study.koreait.dto.req.StudyReqDto;
import com.study.koreait.exception.StudentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

// RestController 역할 부여 - json만 조달하겠다.
// Controller - html파일을 조달해주겠다.
@RestController
@Slf4j // 로깅용 어노테이션
@RequestMapping("/study")
public class StudyController1 {
    // 톰캣의 주소: localhost:8080
    /*
        HTTP 통신 - 웹에서 데이터 주고받는 규칙
        1. 요청/응답에 대한 자바객체
        2. 요청의 경우, 메서드 존제
        - GET: 데이터 조회요청(body가 없음, url의 쿼리스트링으로 전달)
        - POST: 데이터 생성요청
        - DELETE: 데이터 삭제요청
        - PUT / PATCH: 데이터 수정요청
    */

    // localhost:8080/study/test1
    // 주소로 get요청시 실행되는 메서드
    @GetMapping("/test1")
    public String test1() {
        log.info("test1 컨트롤러 수신");
        return "양호합니다";
    }

    // 쿼리스트링으로 데이터 전달받기
    // localhost:8080/study/test2?name=홍길동&age=20
    @GetMapping("/test2")
    public Map<String, Integer> test2(@RequestParam("name") String str, Integer age) {
        log.info("test2 수신완료");
        log.info("들어온 데이터: {}, {}", str, age);
        // 일반적으로 JSON <> 자바객체 or Map
        return Map.of(str, age);
    }

    // 실습) StudyController2 생성
    // add 메서드 - 숫자 2개를 쿼리스트링으로 전달받아 더한결과 응답
    // avg 메서드 - 숫자 3개를 쿼리스트링으로 전달받아 평균계산후 응답


    // {경로변수}와 매개변수가 이름이 같으면, 생략가능
    @GetMapping("/test3/{number}")
    public Map<String, Object> getStudentById(@PathVariable("number") int id) {
        List<StudentResDto> dtos = List.of(
                new StudentResDto(1, "피카츄", 10),
                new StudentResDto(2, "라이츄", 20),
                new StudentResDto(3, "파이리", 30),
                new StudentResDto(4, "꼬부기", 40)
        );

        Optional<StudentResDto> optDto = dtos.stream()
                .filter(s -> s.getId() == id)
                .findFirst();

        // 만약에 optDto가 null이면, 예외를 던져주세요.
        // StudentException - 커스텀예외
        // 전역핸들러 클래스에서 처리할수있게 연결
        StudentResDto ddto = optDto
                .orElseThrow(() -> new StudentException("해당 id의 학생은 찾을 수 없습니다", HttpStatus.NOT_FOUND));


        StudentResDto target = null;
        for (StudentResDto dto : dtos) {
            if (dto.getId() == id) {
                target = dto;
                break;
            }
        }

        if (target == null) {
            return Map.of("error", "해당 id학생은 존재하지 않습니다.");
        }


        return Map.of("success", target);
    }

    // get요청을 제외한 모든 요청 메서드들은 body 존재
    // Json을 담아서 서버로 전송
    @PostMapping("/test4")
    public String test4(@RequestBody Map<String, Object> data) {
        // @RequestBody - body에 있는 Json 자바객체로 변환
        // jackson라이브러리가 자동개입
        log.info("test4 컨트롤러 수신");
        log.info("들어온 데이터 : {}", data);

        return "성공";
    }

    @PostMapping("/test5") // dto
    public ResponseEntity<?> test5(@RequestBody StudyReqDto dto) {
        // 필드명과 Json key이름이 동일해야함.
        // *매칭안된다면 null*
        log.info("들어온 data: {}", dto);

        /*
            HTTP 상태코드
            200대 - 성공
            400대 - 요청을 잘못했다
            500대 - 서버가 잘못했다

            200 OK, 201 CREATED
            400 BAD_REQUEST, 401 UNAUTHORIZED, 403 FORBIDDEN, 404 NOT_FOUND
         */

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("성공");
    }


}
