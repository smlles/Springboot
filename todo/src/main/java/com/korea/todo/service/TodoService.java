package com.korea.todo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.korea.todo.model.TodoEntity;
import com.korea.todo.persistence.TodoRepository;

import lombok.extern.slf4j.Slf4j;

//스프리 프레임워크에서 제공하는 어노테이션중 하나로, 
//서비스 레이어에 사용되는 클래스를 명시 할 때 사용
//이 어노테이션을 사용하면, 해당 클래스를 스프링 컨테이너에서 관리하는 빈으로 등록하고
//비즈니스 로직을 처리하는 일을 맡는다.
//Service어노테이션이 있는 클래스는 스프링빈으로 등록되어, 다른 쪽에서 주입되어 사용 할 수 있다.
@Service
@Slf4j
public class TodoService {
	//영속 계층의 클래스를 주입해서 사용 할 수 있다.
	@Autowired
	private TodoRepository repository;
	
	//Create Todo 구현
	//Todo 아이템을 생성하는 기능 구현하기
	//엔티티를 저장하기 위해 save()메서드를 사용하고, 
	//새 Todo리스트를 반환하기 위해 findByUserId()메서드를 사용한다.
	
	//Todo 아이템을 생성하기 위한 비즈니스 로직 작성
	//create()메서드를 작성한다.
	
	//create()의 구성
	//검증 , 엔티티의 유효성
	//검증 로직이 너무 커지면 TodoValidator로 분리
	
	//save()
	//엔티티를 데이터 베이스에 저장
	//로그를 남김
	
	//findByUserId()
	//저장된 엔티티를 포함하는 새리스트를 반환한다. 
	
	
	
	
	
	
	
	
	
	
//	public String testService() {
//		//엔티티 하나 생성
//		TodoEntity entity = TodoEntity.builder().title("My first todo item").build();
//		//TodoEntity 저장 (DB에)
//		repository.save(entity);
//		//TodoEntity검색 (Select)
//		TodoEntity savedEntity = repository.findById(entity.getId()).get();
//		return savedEntity.getTitle();// -> select 된 Title을 가져올 것
//	}
	
	//추가하고 userId를 기준으로 목록을 반환 
	public List<TodoEntity> create(TodoEntity entity){
		//매개변수로 넘어온 entity가 유효한지 검사하기
		validate(entity);
		//DB에 추가
		repository.save(entity);
		log.info("Entity Id : {} is saved",entity.getId());
		//엔티티를 DB에 추가하고 전체 조회해서 반환하기
		return repository.findByUserId(entity.getUserId());
	}
	
	
	public List<TodoEntity> retrive(String userId){
		return repository.findByUserId(userId);
	}
	
	public List<TodoEntity> update(TodoEntity entity){
		//저장할 엔티티가 유효한지 확인
		validate(entity);
		
		//받은 entity id를 이용해 TodoEntity를 가져온다.
		//존재하지 않는 엔티티는 수정 할 수 없음
		
		Optional<TodoEntity> original = repository.findById(entity.getId());
		//original 안에 값이 있으면
		original.ifPresent(todo->{
			//Todo entity가 있다면, 값을 새 Entity 값으로 덮어씌우기
			todo.setTitle(entity.getTitle());
			todo.setDone(entity.isDone());
			
			//DB에 새 값 저장
			repository.save(todo);
			
		});
		//전체 내용을 조회해서 반환
		return retrive(entity.getUserId());
	}
	
	
	public List<TodoEntity> delete(TodoEntity entity){
		validate(entity);
		
		repository.delete(entity);
		
		return repository.findByUserId(entity.getUserId());
	}
	
	
	private void validate(TodoEntity entity) {
		//매개변수로 넘어온 entity가 유효한지 검사하기
			if(entity==null) {
				log.warn("Entity cannot be null");
				throw new RuntimeException("Entity cannot be null");
			}
			if(entity.getUserId()==null) {
				log.warn("Unknown user");
				throw new RuntimeException("Unknown user");	
			}
	}
	
	
	
	
}
//optional
//null값이 나와도 추가적인 처리를 할 수 있는 다양한 메서드를 제공한다.
//1. ifPresent() : 반환된 Optional 객체안에 값이 존재하면 true, 아니면 false
//2. get() : Optional 안의 값이 존재하면, 그 값을 반환 
// 없는데 호출하면 NoSuchElementException 발생
//3. orElse(T other) : 값이 존재하지 않는다면, 기본값 반환
//findById()의 반환형이 Optional인 이유 -> 조회하려는 ID가 존재하지 않을 수 있어서 nullpoint 방지