package com.korea.user.service;

import java.util.List;
import java.util.Optional;
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
	public UserDTO findByEmail(String email){
		UserEntity entity = repository.findByEmail(email);
		return new UserDTO(entity);
	}
	
	//id통해 이름 이메일 수정
	public List<UserDTO> updateUser(UserEntity entity) {
		Optional<UserEntity> userOptional =  repository.findById(entity.getId());
		//사용자가 존재하면 업데이트 로직 실행
		userOptional.ifPresent(userEntity -> {
			//기존 데이터에 세팅
			userEntity.setName(entity.getName());
			userEntity.setEmail(entity.getEmail());
			//새 데이터 저장
			repository.save(userEntity);
		});
		return getAllUsers();
	}
	
}
