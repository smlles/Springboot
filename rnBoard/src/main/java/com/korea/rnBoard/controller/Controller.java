package com.korea.rnBoard.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.korea.rnBoard.DTO.DTO;
import com.korea.rnBoard.DTO.ResponseDTO;
import com.korea.rnBoard.service.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/posts")
@RestController // 화면은 react니까  @responsebody + @controller
//반환값을 그대로 클라이언트에게 전달
public class Controller {

	
	private final Service service;
	
	
//	다보여줘
	@GetMapping
	public ResponseEntity<?> getAllBoard(){
		
		List<DTO> list = service.getAllBoard();
		ResponseDTO<DTO> response = ResponseDTO.<DTO>builder().data(list).build();
		return ResponseEntity.ok(response);
	}
	
//	하나 보여줘
	@GetMapping("/{id}")
	public ResponseEntity<?> getBoard(@PathVariable("id") int id){
		
		List<DTO> list = service.getBoard(id);
		ResponseDTO<DTO> response = ResponseDTO.<DTO>builder().data(list).build();
		return ResponseEntity.ok(response);
	}
//	낙서해야징 ㅎㅎ
	@PostMapping
	public ResponseEntity<?> addPost(@RequestBody DTO dto){
		DTO saved = service.addPost(dto);
		ResponseDTO<DTO> response = ResponseDTO.<DTO>builder().data(List.of(saved)).build();
		return ResponseEntity.ok(response);
		
	}
}
