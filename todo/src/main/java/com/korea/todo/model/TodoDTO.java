package com.korea.todo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TodoDTO {//민감한 정보인 userId가 없어짐
	private String id;
	private String title;
	private boolean done;
	
	public TodoDTO(TodoEntity entity) {
		this.id=entity.getId();
		this.title=entity.getTitle();
		this.done = entity.isDone();
	}
}
