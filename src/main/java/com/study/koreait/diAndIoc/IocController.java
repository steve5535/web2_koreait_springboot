package com.study.koreait.diAndIoc;

import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/ioc")
@Data // RequiredArgsConstructor
public class IocController {
    private final IocService service;

    // 생성자 하나일 경우 Autowired 생략할 수 있음.
    // lombok의 @Data와 같이 쓰인다

    @GetMapping("/test")
    public ResponseEntity<?> diTest() {
        Integer total =  service.getTotal();
        Double avg = service.getAvg();
        /*
            {
                "total": 430,
                "average": 85.5
            }
            응답할 수 있게 적절한 service 메서드 작성 및 응답코드를 작성해주세요
         */

        Map<String, Object> resMap = Map.of(
                "total: ", total,
                "average: ", avg
        );

        return ResponseEntity.ok(resMap);
    }
}
