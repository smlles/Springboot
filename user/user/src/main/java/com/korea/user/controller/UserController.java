package com.korea.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.korea.user.model.UserDTO;
import com.korea.user.model.UserEntity;
import com.korea.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
	
	@Autowired
	private final UserService service;
	
	//생성하기
	@PostMapping
	public ResponseEntity<?> createUser(@RequestBody UserDTO dto){
		UserEntity entity = UserDTO.toEntity(dto);
		List<UserDTO> users = service.create(entity);
		return ResponseEntity.ok(users);
	}
	
	//조회하기
	@GetMapping
	public ResponseEntity<?> getAllUsers(){
		List<UserDTO> users = service.getAllUsers();
		return ResponseEntity.ok().body(users);
	}
	
	//이메일로 찾기
	@GetMapping("/users/{email}")
	public ResponseEntity<?> findByEmail(@PathVariable String email){
		UserDTO  user = service.findByEmail(email);
				
		return ResponseEntity.ok(user);
	}
	
	//id를 통해 이름 이메일 수정
	@PutMapping
	public ResponseEntity<?> updateUser(@RequestBody UserDTO dto){
		UserEntity entity = UserDTO.toEntity(dto);
		List<UserDTO> updateUser = service.updateUser(entity);
		return ResponseEntity.ok(updateUser);
		
	}
	
}
