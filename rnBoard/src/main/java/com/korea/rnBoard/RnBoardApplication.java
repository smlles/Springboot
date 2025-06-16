package com.korea.rnBoard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication //component Scan -> component 어노테이션 전부 메모리에 올리기
//@repository , @service, @controller -> component 하위 컴포넌트
public class RnBoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(RnBoardApplication.class, args);
	}

}
