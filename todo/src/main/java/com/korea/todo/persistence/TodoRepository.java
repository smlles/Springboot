package com.korea.todo.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.korea.todo.model.TodoEntity;

@Repository
//주로 DB와 상호작용하는 클래스에서사용되며, CRUD와 같은 데이터베이스 작업을 처리하는데 사용된다.
//@Component의 자식 어노테이션이므로, 자동으로 bean으로 등록된다.
//다른 계층에서 주입받아 사용 할 수 있다.
//JpaRepository<T, ID>
//Spring data JPA에서 제공하는 기본 인터페이스로,  JPA를 사용하여 DB작업ㅇ을
//쉽게 처리 할 수 있도록 도와주는 역할을 한다.
//T = 엔티티 클래스 / ID = 엔티티 클래스의 기본 키 타입
public interface TodoRepository extends JpaRepository<TodoEntity, String>{
	
	//t.userId=?1
	//userId 필드가 주어진 파라미터와 일치하는 TodoEntity 객체들을 조회하는 조건문
//	@Query("Select T from TodoEntity t where t.userId=?1")
//	List<TodoEntity> findByUserIdQuery(String userId);
	
	List<TodoEntity> findByUserId(String userId);
	
	//JPQL특징
	//DB테이블 대신 JPA 엔티티 객체를 대상으로 쿼리실행
	//테이블 이름이나 컬럼 대신 엔티티 클래스의 이름과 필드를 사용하여 쿼리를 작성한다.
	//이 때문에 JPQL쿼리는 DB독립적이며 DB의 스키마에 의존하지 않는다.
	//JPQL은 SQL과 매우 유사한 문법을 사용하지만, 엔티티 객체를 다룬다는 점에서 차이가 있다.
	//ex) SQL = SELECT * FROM users / JPQL = SELECT u FROM Users u
}
