package com.korea.user.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
	//사용자 검색 기능이 필요하면 메서드 정의 가능
	
	//email 기준으로 찾기 //List로 찾아도 되기는 함
	UserEntity findByEmail(String email);
}
