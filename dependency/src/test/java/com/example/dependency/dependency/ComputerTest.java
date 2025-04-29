package com.example.dependency.dependency;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j //롬복에 있는 콘솔로그를 쓸 수 있는 어노테이션
public class ComputerTest {
	
//	@Autowired
	Coding coding;
	
	//생성자주입
	@Autowired
	public ComputerTest(Coding coding) {
		this.coding = coding;
	}
	
	
	@Test
	public void computerTest() {
		//스프링에서 만든 객체가 아니다.
		//Coding coding = new Coding();
		
		//스프링이 메모리에 올려놓은 객체를 변수에 주입
		
		//로그에 들어갈 수 있는 내용은 문자열 밖에 안된다.
		log.info("결과:{}",coding.getComputer().getRam());
	}
}
