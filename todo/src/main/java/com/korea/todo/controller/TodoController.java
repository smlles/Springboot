package com.korea.todo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.korea.todo.model.ResponseDTO;
import com.korea.todo.model.TodoDTO;
import com.korea.todo.model.TodoEntity;
import com.korea.todo.service.TodoService;

@RestController
@RequestMapping("todo")
public class TodoController {

	// 실행 할 때 Service객체가 필드로 직접 주입이 된다.
	@Autowired
	TodoService service;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              
	// 요청을 통해서 넘어오는 정보는 요청 본문에 담겨서 온다.
	@PostMapping
	public ResponseEntity<?> createTodo(@RequestBody TodoDTO dto) {
		try {
			String temporaryUserId = "temporary-user";// 임시 userId

			// TodoDTO객체를 TOdoEntity객체로 변환
			TodoEntity entity = dto.toEntity(dto);

			// id값을 명시적으로 null로 설정하여 엔티티가 새로운 데이터임을 보장하기
			entity.setId(null);

			// 임시 유저id 설정
			// 지금은 인증, 인가 기능이 없으므로 한명만 로그인 없이 사용 가능한
			// 어플리케이션이라고 가정
			entity.setUserId(temporaryUserId);

			// 서비스 계층에 있는 create 메서드를 호출하여,
			// Todoentity를 데이터베이스에 저장하는 작업을 수행
			// 이 메서드는 추가하는 것 만이 아니라, 저장된 TodoEntity 개체들을
			// 저장한 리스트를 반환한다
			List<TodoEntity> entities = service.create(entity);

			// 자바 스트림을 이용해 반환된 엔티티 리스트를 TodoDTO리스트로 변환
			//Todo DTO::new -> TodoDTO 생성자의 호출
			List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
			
			//변환된 TodoDTO리스트를 이용해 responseDTO를 초기화
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
			
			return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			//만약 예외가 발생한다면, dto대신 에러에 메세지를 넣어 반환
			String error = e.getMessage();
			
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
			
			return ResponseEntity.badRequest().body(response);
		}
	}

	//getMapping 조회
	@GetMapping("/todo") //retrive
	public ResponseEntity<?> retriveTodoList(){
		String temporaryUserId = "temporary-user";
		List<TodoEntity> entities = service.retrive(temporaryUserId);
		List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
		
		ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
		return ResponseEntity.ok().body(response);
	}
	
	@PutMapping
	public ResponseEntity<?> updateTodo(@RequestBody TodoDTO dto){
		String temporaryUserId = "temporary-user";
		//DTO를 entity로 변환
		TodoEntity entity = TodoDTO.toEntity(dto);
		entity.setUserId(temporaryUserId);
		
		//서비스 레이어의 update 메서드를 이용해 entity 업데이트하기
		List<TodoEntity> entities = service.update(entity);
		List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
		
		//스트림을 이용해 반환된 엔티티 리스트를 TodoDTO리스트로 반환한다.
		ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
		return ResponseEntity.ok().body(response);
	}
	//삭제하기
	//delete()를 이용해 db에서 삭제하고
	//findbyUserID()를 사용해 다시 조회
	@DeleteMapping
	public ResponseEntity<?> deleteTodo(@RequestBody TodoDTO dto){
		String temporaryUserId = "temporary-user";
		//DTO를 ENTITY로 변환
		TodoEntity entity = TodoDTO.toEntity(dto);
		//임시 아이디 설정
		entity.setUserId(temporaryUserId);
		
		//Entity에서 삭제
		List<TodoEntity> entities = service.delete(entity);
		//stream을 사용해 entity에서 다시 DTO로
		List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
		
		//DTOS를 RESPONSE로 다시 담아서 반환
		ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
		return ResponseEntity.ok().body(response);
		
	}
	
	// 주입 받은 객체로 메서드를 실행하면 된다.
//	@GetMapping("/test")
//	public ResponseEntity<?> testTodo(){
//		//service클래스에 있는 메서드 호출
//		String str = service.testService();
//		List<String> list = new ArrayList<>();
//		list.add(str);
//		ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
//		return ResponseEntity.ok().body(response);
//	}
}
