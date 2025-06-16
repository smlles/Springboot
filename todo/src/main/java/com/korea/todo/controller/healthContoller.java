package com.korea.todo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class healthContoller {
	
	
//	AWS 로드밸런서는 루트경로인 '/'에 http요청을 보내서
	//어플리 케이션이 동작하는지 확인한다.
	//EB는 이를 기반으로 어플리케이션이 실행중인지, 주의가 필요한지 확인한다.
	@GetMapping("/")
	public String healthCheck() {
		
		return "The service is up and running...";
	}

}
