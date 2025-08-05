package com.edutalk.edutalk.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edutalk.edutalk.model.ChatEntity;

@Repository
public interface ChatRepository extends JpaRepository<ChatEntity, UUID> {
	// 현재는 기본적인 CRUD 기능만 필요하므로 추가적인 메소드는 정의하지 않습니다.
	// 추후 특정 조건으로 채팅방을 검색해야 할 경우, 여기에 쿼리 메소드를 추가할 수 있습니다.
	// 예: List<ChatEntity> findByStudentId(String studentId);
	
	// 특정 identy와 강사 ID에 해당하는 채팅방 목록 조회
	List<ChatEntity> findByIdentyAndInstructorID(String identy, String instructorID);

	// 특정 identy와 학생 ID에 해당하는 채팅방 목록 조회
	List<ChatEntity> findByIdentyAndStudentID(String identy, String studentID);
}