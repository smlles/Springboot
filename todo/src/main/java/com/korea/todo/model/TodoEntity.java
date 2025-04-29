package com.korea.todo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TodoEntity {
	private String id; //객체의 ID
	private String userId; //객체를 생성한 유저의 ID
	private String title; // Todo의 타이틀
	private boolean done;// Todo의 완료 여부 
}
