package com.korea.member.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.korea.member.DTO.MemberDTO;
import com.korea.member.DTO.ResponseDTO;
import com.korea.member.model.MemberEntity;
import com.korea.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/members")
@RequiredArgsConstructor
@RestController
public class MemberController {

	//주입
	private final MemberService service;
	
	//전체 회원 조회 
		@GetMapping
		public ResponseEntity<?> allMember(){
			
			List<MemberDTO> list = service.allMember();
			ResponseDTO<MemberDTO> response = ResponseDTO.<MemberDTO>builder().data(list).build();
			return ResponseEntity.ok(response);
		}
		//특정 회원 조회
		@GetMapping("/{email}")
		public ResponseEntity<?> findByEmail(@PathVariable String email){
			
		List<MemberDTO> list = service.findByEmail(email);
		ResponseDTO<MemberDTO> response = ResponseDTO.<MemberDTO>builder().data(list).build();
		return ResponseEntity.ok(response);
			
			
		}
		/// 회원 추가
		@PostMapping
		public ResponseEntity<?> createMember(@RequestBody MemberDTO dto){
			MemberEntity entity = MemberDTO.toEntity(dto);
			//=MemberEntity member = MemberEntity.builder()
			//									.name(dto.getName()
			//									.email(dto.getEmail()
			//									.password(dto.getPassword()
			//									.build();
			//=MemberEntity.builder -> new MemberEntity(); -> setName 수작업  
			List<MemberDTO> list = service.createMember(entity);
			ResponseDTO<MemberDTO> response = ResponseDTO.<MemberDTO>builder().data(list).build();
			return ResponseEntity.ok(response);
		}
		
//		@PostMapping
//		public ResponseEntity<?> loginMember(@ReqestBody MemberDTO dto){
//			MemberEntity entity = 
//		}
		//이메일을 통해 비밀번호 변경
		@PutMapping("/{email}/password")
		public ResponseEntity<?> updateMember(@PathVariable String email, @RequestBody MemberDTO dto){
			String newPassword = dto.getPassword();
			List<MemberDTO> list = service.updatePasswordByEmail(email,newPassword);
			ResponseDTO<MemberDTO> response = ResponseDTO.<MemberDTO>builder().data(list).build();
			return ResponseEntity.ok(response);
			
		}
		//회원 아이디 삭제
		@DeleteMapping("/{id}")
		public ResponseEntity<?> deleteMember(@PathVariable int id){
			
			List<MemberDTO> list = service.deleteMember(id);
			ResponseDTO<MemberDTO> response = ResponseDTO.<MemberDTO>builder().data(list).build();
			return ResponseEntity.ok(response);
		}
}
