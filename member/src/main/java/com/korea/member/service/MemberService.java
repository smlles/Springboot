package com.korea.member.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.korea.member.DTO.MemberDTO;
import com.korea.member.model.MemberEntity;
import com.korea.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository repository;
	
	//전체 회원 조회 
	public List<MemberDTO> allMember(){
		
		return repository.findAll().stream().map(MemberDTO::new).collect(Collectors.toList());
		
	}
	//특정 회원 조회

	public List<MemberDTO> findByEmail(String email){
		Optional<MemberEntity> option = repository.findByEmail(email);
		MemberDTO dto = null;
		if(option.isPresent()) {
			MemberEntity entity = option.get();
			dto=MemberDTO.builder()
					.id(entity.getId())
					.name(entity.getName())
					.email(entity.getEmail())
					.password(entity.getPassword())
					.build();
			
		}
		return Arrays.asList(dto);
		
	}
	/// 회원 추가
	public List<MemberDTO> createMember(MemberEntity entity){
		repository.save(entity);
		
		return repository.findAll().stream().map(MemberDTO::new).collect(Collectors.toList());
	}
	
	//이메일을 통해 비밀번호 변경
	public List<MemberDTO> updatePasswordByEmail(String email, String newPassword) {
		Optional<MemberEntity> option = repository.findByEmail(email);
		if(option.isPresent()) {
			MemberEntity entity = option.get();
			entity.setPassword(newPassword);
			repository.save(entity);
		}
		 return allMember();
	}
	//회원 아이디 삭제
	public List<MemberDTO> deleteMember(int id){
		if(repository.existsById(id)) {
			repository.deleteById(id);
		} else {
			throw new RuntimeException("원래 없는 회원");
		}
		return allMember();
	}

	

	
}
