package com.study.koreait.diAndIoc;

import org.springframework.stereotype.Repository;

import java.util.List;

/*
    스프링부트에서는 객체생성(new) and di의 주도권이 프레임워크에 있음
    객체의 생성 -> 사용 -> 소멸 타이밍을 스프링부트가 알아서 결정

    how? IOC컨테이너라는게 존재
    1. SpringApplication.ran()이 실행되면, 컨테이너를 만든 후 스캔 실행
    2. 스캔된 class 객체들을 싱글톤으로 생성하여 컨테이너에 미리 보관
    3. 만들어진 객체들을 Bean이라고 함.
    4. 필요한 곳에 알아서 주입 - @Autowired

    @Component
        @RestController
        @Repository
        @Service
        @Configuration
*/

@Repository
public class IocRepository {

    private List<Integer> scores = List.of(100, 90, 80, 70);

    public List<Integer> getScores() {
        return scores;
    }
}
