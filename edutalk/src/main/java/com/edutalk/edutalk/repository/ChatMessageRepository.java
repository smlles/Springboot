package com.edutalk.edutalk.repository;

import com.edutalk.edutalk.model.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

	/*
	 * 특정 채팅방의 모든 메시지를 시간 순서대로 조회합니다.
	 * 
	 * @param chatRoomId 채팅방 ID (ChatEntity의 uid)
	 * 
	 * @return List<ChatMessage>
	 */
	List<ChatMessage> findByChatRoomIdOrderByTimestampAsc(String chatRoomId);

	/*
	 * 특정 채팅방의 모든 메시지를 삭제합니다.
	 * 
	 * @param chatRoomId 삭제할 채팅방 ID
	 */
	void deleteByChatRoomId(String chatRoomId);
}