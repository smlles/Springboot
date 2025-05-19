package com.example.user2.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.user2.DTO.UserDTO;
import com.example.user2.model.UserEntity;
import com.example.user2.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository repository;

	public List<UserEntity> findAll() {
		return repository.findAll();
		
	}

	public List<UserEntity> createUser(UserDTO dto) {
		UserEntity entity = dto.toEntity(dto);
		repository.save(entity);
		
		return findAll();
		
	}
}
