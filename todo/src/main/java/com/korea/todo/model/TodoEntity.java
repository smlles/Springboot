package com.korea.todo.model;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.Id;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor //엔티티클래스로 지정시 매개변수 없는 생성자가 필요하다.
//내부적으로 Class.newInstance()와 비슷한 방식으로 객체를 만드는데
//매개변수가 있는 생성자만 있으면, 어떤 인자를 넘겨야 하는지 몰?루라서 예외가 발생한다.
@AllArgsConstructor
@Data
@Entity
//자바를 JPA 엔티티로 지정하기 위해 사용
@Table(name="Todo")
//Todo 테이블을 찾아서 매핑하거나, 생성을 해준다.
public class TodoEntity {
	@jakarta.persistence.Id //엔티티의 기본키 (Primary key)필드를 나타냄
	@GeneratedValue(generator="system-uuid") //기본키 값을 자동 생성하도록 지시
	//generator를 써서 이름을 붙혀둔 @GenericGenerator를 참조
	@GenericGenerator(name="system-uuid",strategy="uuid")
	//Hibernate가 제공하는 커스텀 생성 전략을 정의
	//name="system-uuid" : 이 이름을 @GenericGenerator에서 참조
	//strategy="uuid" : uuid 문자열을 기본키로 설정
	private String id; //객체의 ID
	private String userId; //객체를 생성한 유저의 ID
	private String title; // Todo의 타이틀
	private boolean done;// Todo의 완료 여부 
}

