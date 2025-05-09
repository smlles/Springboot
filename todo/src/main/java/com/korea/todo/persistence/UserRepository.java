package com.korea.todo.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.korea.todo.model.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, String>{
	UserEntity findByUsername(String username);//username값으로 데이터를 한 건 찾아 반환
	boolean existsByUsername(String username); //username이 존재하면 true, 아니면 false
	UserEntity findByUsernameAndPassword(String username, String password); 
	//username과 비밀번호 이용해서 하나 찾음, And니까 두 필드 모두 만족하는 데이터를 찾는 조건으로 쿼리 생성
}
