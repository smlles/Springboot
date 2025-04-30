package com.korea.todo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.korea.todo.model.TodoEntity;
import com.korea.todo.persistence.TodoRepository;

//스프리 프레임워크에서 제공하는 어노테이션중 하나로, 
//서비스 레이어에 사용되는 클래스를 명시 할 때 사용
//이 어노테이션을 사용하면, 해당 클래스를 스프링 컨테이너에서 관리하는 빈으로 등록하고
//비즈니스 로직을 처리하는 일을 맡는다.
@Service
public class TodoService {
	
	@Autowired
	private TodoRepository repository;
	
	public String testService() {
		//엔티티 하나 생성
		TodoEntity entity = TodoEntity.builder().title("My first todo item").build();
		//TodoEntity 저장 (DB에)
		repository.save(entity);
		//TodoEntity검색 (Select)
		TodoEntity savedEntity = repository.findById(entity.getId()).get();
		return savedEntity.getTitle();// -> select 된 Title을 가져올 것
	}
}
//optional
//null값이 나와도 추가적인 처리를 할 수 있는 다양한 메서드를 제공한다.
//1. isPresent() : 반환된 Optional 객체안에 값이 존재하면 true, 아니면 false
//2. get() : Optional 안의 값이 존재하면, 그 값을 반환 
// 없는데 호출하면 NoSuchElementException 발생
//3. orElse(T other) : 값이 존재하지 않는다면, 기본값 반환
//findById()의 반환형이 Optional인 이유 -> 조회하려는 ID가 존재하지 않을 수 있어서 nullpoint 방지