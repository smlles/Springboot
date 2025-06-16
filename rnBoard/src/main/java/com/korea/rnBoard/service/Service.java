package com.korea.rnBoard.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.korea.rnBoard.DTO.DTO;
import com.korea.rnBoard.domain.Entity;
import com.korea.rnBoard.repository.Repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@org.springframework.stereotype.Service
@Slf4j
@RequiredArgsConstructor //final 혹은 @notnull로 정의된 필드를 매개변수로 갖는 생성자 생성
public class Service {
	
	private final Repository repository;

	
//	다보여줘
	public List<DTO> getAllBoard() {
		
		return repository
				.findAll()
				.stream()
				.map(DTO::new)
				.collect(Collectors.toList());
	}

	
//	하나보여줘
	public List<DTO> getBoard(int id) {
		Optional<Entity> option = repository.findById(id);
//		optional : null값을 안전하게 다루기 위한 Wrapper 클래스;
//		null로 생길 에러를 방지, 있을수도 없을수도 있는 것을 정확히 표현하는 법
		DTO dto = null;
		if(option.isPresent()) {
			Entity entity=option.get();
			dto=DTO.builder()
					.id(entity.getId())
					.title(entity.getTitle())
					.author(entity.getAuthor())
					.description(entity.getDescription())
					.time(entity.getTime())
					.views(entity.getViews())
					.build();
		}
		return Arrays.asList(dto);
	}

// 낙서
	public DTO addPost(DTO dto) {
		Entity entity = DTO.ToEntity(dto);
		Entity saved = repository.save(entity);
		return new DTO(saved);
	}
	
	
}
