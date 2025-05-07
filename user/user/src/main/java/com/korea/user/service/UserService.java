package com.korea.user.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.korea.user.model.UserDTO;
import com.korea.user.model.UserEntity;
import com.korea.user.model.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor //final이나 @NonNull이 붙은 필드를 생성자의 매개변수로 포함)
public class UserService {
	
	@Autowired
	private final UserRepository repository;
	
	//= public UserService(userRepository repository){
		//	this.repository = repository;
		//}
	
	
	
	//만들기
	public List<UserDTO> create(UserEntity entity){
		
		repository.save(entity); //DB에 저장
		return repository.findAll().stream().map(UserDTO::new).collect(Collectors.toList());
	}
	
	//전체 조회하기
	public List<UserDTO> getAllUsers(){
		return repository.findAll().stream().map(UserDTO::new).collect(Collectors.toList());
	}
	
	//이메일로 조회
	public List<UserDTO> findByEmail(String email){
		
		return null;
		
	}
	
}
