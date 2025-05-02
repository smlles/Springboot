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
	
	//생성자(TodoEntity-> todoDTO)
	public TodoDTO(TodoEntity entity) {
		this.id=entity.getId();
		this.title=entity.getTitle();
		this.done = entity.isDone();
	}
	
	//TodoDTO->TodoEntity
	public static TodoEntity toEntity(TodoDTO dto) {
		return TodoEntity.builder().id(dto.getId()).title(dto.getTitle()).done(dto.isDone()).build();
	}
}
