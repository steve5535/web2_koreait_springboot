package com.study.koreait.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// IOC 컨테이너에 직접 메서드형식으로 bean 등록할 수 있게됨
@Configuration
public class AppConfig {
    // 컴포넌트 스캔 범위 밖인 라이브러리 클래스들
    // -> bean 등록을 해야할 때

//    @Bean // bean 등록메서드
//    public ObjectMapper objectMapper() {
//        return new ObjectMapper();
//    }
}
