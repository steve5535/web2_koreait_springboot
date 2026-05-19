package com.study.koreait;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KoreaitApplication {

	// 스프링부트 서버 진입점 - 유일한 main메서드
	// .java파일들은 main함수가 있는 클래스의 안쪽에 존재해야한다.
	public static void main(String[] args) {
		SpringApplication.run(KoreaitApplication.class, args);
	}

}
