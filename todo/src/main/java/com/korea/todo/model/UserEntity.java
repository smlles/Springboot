package com.korea.todo.model;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //lombok을 사용하여 getter,setter,tostring,equals,hashcode를 자동 생성
@Entity //JPA에서 엔티티 클래스로 사용할 것을 명시
@Builder //lombok의 Builder패턴을 사용하여 객체를 생성할 수 있게 한다.
@NoArgsConstructor //기본 생성자 생성
@AllArgsConstructor //모든 필드를 인자로 갖는 생성자를 자동 생성
@Table(name = "user_entity")
//테이블에서 username컬럼에 유니크 제약조건을 설정했다.
public class UserEntity {
	
	@Id //JPA에서 해당 필드가 테이블의 PK임을 나타냄
	@GeneratedValue(generator="system-uuid") //id값 자동으로 넣기, 값의 생성은 
	//system-uuid라는 이름을 가진 어노테이션에게 맡긴다.
	@GenericGenerator(name="system-uuid",strategy="uuid")
	//uuid : 128비트 길이의 고유 식별자
	private String id; //유저에게 고유하게 부여되는 ID, uuid로 생성되며 완전히` 고유한 값
	
	@Column(nullable=false,unique=true) //not null 제약
	private String username; //아이디로 사용할 username, 이메일일수도 있고 문자열일수도 있다.
	
	private String password; //유저 패스워드
	
	private String role; //유저 권한 ex)"관리자","일반 사용자"같은 값이 들어 갈 수 있다.
	
	private String authProvider; //소셜 로그인 할 때 사용할 유저 정보의 제공자
	
	
	
	
	

}
