package com.korea.todo.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.korea.todo.model.UserEntity;
import com.korea.todo.persistence.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

	//repository 생성자 주입
	private final UserRepository repository; 
	//autowired -> 필드주입 
	//final -> 생성자 주입
	
	//유저 추가
	//회원 가입 화면에서 넘어오는 정보
	public UserEntity create(UserEntity entity) {
		//주어진 유저 엔티티가 null이거나, 또는 username이 null이면 예외를 발생시킨다.
		if(entity.getUsername()==null||entity==null) {
			throw new RuntimeException("Invalied arguments");
		}
		//entity에서  username을 가져옴
		final String username = entity.getUsername();
		//주어진 username이 존재하는 경우, 경로 로그를 남기고 예오 ㅣ던지기
		
		if(repository.existsByUsername(username)) {
			log.warn("Username already exsist{}",username);
			throw new RuntimeException("Username already exists");
		}
		//중복되지 않은 username이라면 entity를 DB에 추가하기
		return repository.save(entity);
	}
	
	//주어진 username과 password를 이용해서 entity를 조회한다.
	public UserEntity getByCredential(String username, String password,final PasswordEncoder encoder) {
		final UserEntity originalUser = repository.findByUsername(username);
		if(originalUser!=null && encoder.matches(password, originalUser.getPassword())) {
			return originalUser;
		}
	
		return null;
	}
}
