package com.example.user2.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.user2.DTO.UserDTO;
import com.example.user2.model.UserEntity;
import com.example.user2.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserController {
	private final UserService service;
	
	
	@GetMapping
	public ResponseEntity<?> getUser(){
		List<UserEntity> entity = service.findAll();
		
		return ResponseEntity.ok(entity);
	}
	
	@PostMapping
	public ResponseEntity<?> createUser(@RequestBody UserDTO dto){
		List<UserEntity> entity = service.createUser(dto);
		
		List<UserDTO> dtos = entity.stream().map(UserDTO::fromEntity).collect(Collectors.toList());
		return ResponseEntity.ok(dtos);
		
		
	}
}
