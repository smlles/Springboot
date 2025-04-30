package com.korea.todo.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.korea.todo.model.ResponseDTO;
import com.korea.todo.model.TestRequestBodyDTO;

@RestController
//데이터를 반환하는 컨트롤러로 사용
//JSON이나 XML형식의 데이터 반환
//@Controller, @ResponseBody 두개의 어노테이션의 결합
//@ResponseBody는 메서드의 반환값을 HTTP ResponseBody로 직렬화해 클라이언트에게 전달
@RequestMapping("test") // test 주소로 요청이 들어왔을 때 현재 컨트롤러로 들어올 수 있게 함
//컨트롤러의 공통주소를 가짐
public class TestController {
//주소 겹치지 말기 
	@GetMapping("/testGetMapping") // GET 으로 요청이 들어왔을 때 요청을 받아서 아래 메서드를 ㅅ실행
	public String testController() {
		return "Hello world";
	}

	// get으로 요청이 들어오고, 주소가 맞을 때 메서드 실행
	@GetMapping("/test2") // 세부주소
	public String testController2() {
		return "안녕 안녕";
	}

	// 배열을 통해 여러개의 요청 URL를 받을 수 있다.
	@GetMapping("/users/{id}")
	public String getUserById(@PathVariable(name = "id", required = false) String userId) {
		return "User Id : " + userId;
	}

	@GetMapping("/users/{userId}/orders/{orderId}")
	public String getOrderByUserAndOrderId(@PathVariable("userId") long id, @PathVariable("orderId") long orderId) {
		return "User ID : " + id + ", Order ID : " + orderId;
	}

	// 정규식을 써서 변수 형식을 제한할 수 있다.
	@GetMapping("/users/{userId:[0-9]{3}}")
	public String getOrderByUser(@PathVariable("userId") long id) {
		return "User ID : " + id;
	}

	// 쿼리스트링의 key와 매개변수의 변수명이 일치하면 value값을 안줘도 된다.
	// 일치한다면 말이지
	@GetMapping("/users")
	// public String getUserById(@RequestParam(required=false) long id)
	// 값을 반드시 넣지 않아도 에러가 나지는 않는다.
	// public String getUserById(@RequestParam(defaultValue=0) long id)
	// 값이 넘어오지 않았을 때, 기본값을 설정 할 수 있다.
	public String getUserById(@RequestParam(value = "id", defaultValue = "0") long id) {
		return "User ID : " + id;
	}

	@GetMapping("/search")
	public String serch(@RequestParam("query") String query, @RequestParam("page") int page) {
		return "Search query : " + query + " , page : " + page;
	}
	
	@PostMapping("/submitForm")
	public String submitForm(@RequestParam("name") String name,
							@RequestParam("email") String email) {
		return "Form submitted : Name = " + name + ", Email = "+email;
	}
	
	@GetMapping("/testRequestBody")
	//JSON 형식으로 전달되는 데이터를 TestRequestBodyDTO형식으로 만들어서 넣어준다.
	//{"id"=123, "message":"hello ?"} -> 키값이 일치해야함
	public String testRequestBody(@RequestBody TestRequestBodyDTO dto) {
		return "ID : "+dto.getId() + ", Message : "+dto.getMessage();
	}
	
	@GetMapping("/testResponseBody")
	public ResponseDTO<String> testResponseBody(){
		List<String> list = Arrays.asList("하나","둘","셋");
		ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
		//String error, List<String> list가 담겨있는, responseDTO객체를 반환
		return response;
	}
	
	@GetMapping("/testResponseEntity")
	public ResponseEntity<?> testResponseEntity(){
		List<String> list = new ArrayList<>();
		list.add("Hello World! I'm ResponseEntity. And you got 400");
		ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
		return ResponseEntity.badRequest().body(response);
	}
	//ResponseDTO를 반환하는 것과 큰 차이는 없지만
	//헤더와 HTTPStatus를 조작 할 수 있다는 점이 다르다
}
