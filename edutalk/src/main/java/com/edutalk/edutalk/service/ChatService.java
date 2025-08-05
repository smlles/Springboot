package com.edutalk.edutalk.service;


  import com.edutalk.edutalk.dto.ChatRoomDto;
  import com.edutalk.edutalk.model.ChatEntity;
  import com.edutalk.edutalk.model.ChatMessage;
  import com.edutalk.edutalk.repository.ChatMessageRepository;    
  import com.edutalk.edutalk.repository.ChatRepository;
  import lombok.RequiredArgsConstructor;
  import org.springframework.stereotype.Service;
  import org.springframework.transaction.annotation.Transactional;
  import org.springframework.web.multipart.MultipartFile;
  import java.io.IOException; // throws IOException 때문에 필요
  // import java.nio.file.Files; // 파일 저장 로직 주석 해제 시 필요
  // import java.nio.file.Path;   // 파일 저장 로직 주석 해제 시 필요
  // import java.nio.file.Paths;  // 파일 저장 로직 주석 해제 시 필요
  // import java.nio.file.StandardCopyOption; // 파일 저장 로직 주석 해제 시 필요   
  import java.time.LocalDateTime;
  import java.util.List;
  import java.util.UUID;
  import java.util.stream.Collectors;


  @Service
  @RequiredArgsConstructor
  public class ChatService {

      private final ChatRepository chatRepository; // PostgreSQL
      private final ChatMessageRepository chatMessageRepository; // MongoDB


      // 새로운 채팅방 생성 또는 기존 채팅방 조회
      @Transactional
      public ChatRoomDto getOrCreateChatRoom(String identy, String instructorId, String instructorName, String studentId, String studentName) {
          // TODO: 실제로는 identy, instructorId, studentId를 조합하여 기존 채팅방을 찾아야 합니다.
          // 현재는 간단화를 위해 항상 새로운 채팅방을 생성하는 것으로 가정합니다.
          // Prisma 스키마의 unique 제약조건을 고려하여 findBy... 메소드를 구현해야 합니다.
          // 예: chatRepository.findByIdentyAndInstructorIDAndStudentID(...)


          ChatEntity chatEntity = new ChatEntity();
          chatEntity.setIdenty(identy);
          chatEntity.setInstructorID(instructorId);
          chatEntity.setInstructorName(instructorName);
          chatEntity.setStudentID(studentId);
          chatEntity.setStudentName(studentName);
 

          chatEntity = chatRepository.save(chatEntity);

          return convertToChatRoomDto(chatEntity, null); // 초기에는 마지막 메시지 없음      
      }


      // 모든 채팅방 목록 조회 (간단화된 버전)
      @Transactional(readOnly = true)
      public List<ChatRoomDto> getAllChatRooms() {
          List<ChatEntity> chatEntities = chatRepository.findAll();
          return chatEntities.stream()
                  .map(chatEntity -> {
                      // 각 채팅방의 마지막 메시지를 MongoDB에서 가져와 DTO에 포함
                      List<ChatMessage> messages = chatMessageRepository.findByChatRoomIdOrderByTimestampAsc(chatEntity.getUid().toString());
                      ChatMessage lastMessage = messages.isEmpty() ? null : messages.get(messages.size() - 1);
                      return convertToChatRoomDto(chatEntity, lastMessage);
                  })
                  .collect(Collectors.toList());
      }
      
      /**
       * 특정 사용자의 채팅 목록을 조회합니다.
       * @param identy 사용자의 identy (계약사명/기관명)
       * @param userType 사용자의 타입 (teacher 또는 student)
       * @param userId 사용자의 고유 ID (강사 ID 또는 학생 ID)
       * @return 해당 사용자의 채팅방 목록 DTO
       */
      @Transactional(readOnly = true)
      public List<ChatRoomDto> getChatListForUser(String identy, String userType, String userId) {
          List<ChatEntity> chatEntities;

          if ("teacher".equals(userType)) {
              // 강사인 경우, instructorID가 userId와 일치하는 채팅방 조회
              chatEntities = chatRepository.findByIdentyAndInstructorID(identy, userId);
          } else if ("student".equals(userType)) {
              // 학생인 경우, studentID가 userId와 일치하는 채팅방 조회
              chatEntities = chatRepository.findByIdentyAndStudentID(identy, userId);
          } else {
              // 유효하지 않은 userType인 경우 빈 목록 반환 또는 예외 처리
              return List.of();
          }

          return chatEntities.stream()
                  .map(chatEntity -> {
                      // 각 채팅방의 마지막 메시지를 MongoDB에서 가져와 DTO에 포함
                      List<ChatMessage> messages =
                              chatMessageRepository.findByChatRoomIdOrderByTimestampAsc(chatEntity.getUid().toString());
                      ChatMessage lastMessage = messages.isEmpty() ? null : messages.get(messages.size() - 1);
                      return convertToChatRoomDto(chatEntity, lastMessage);
                  })
                  .collect(Collectors.toList());
      }

      // 특정 채팅방의 메시지 조회
      @Transactional(readOnly = true)
      public List<ChatMessage> getChatMessages(UUID chatRoomUid) {
          return chatMessageRepository.findByChatRoomIdOrderByTimestampAsc(chatRoomUid.toString());
      }


      // 메시지 저장
      @Transactional
      public ChatMessage saveMessage(UUID chatRoomUid, String senderId, String senderName, String messageContent) {
          ChatMessage chatMessage = new ChatMessage();
          chatMessage.setChatRoomId(chatRoomUid.toString());
          chatMessage.setSenderId(senderId);
          chatMessage.setSenderName(senderName);
          chatMessage.setMessage(messageContent);
          chatMessage.setTimestamp(LocalDateTime.now());


          // MongoDB에 메시지 저장
          ChatMessage savedMessage = chatMessageRepository.save(chatMessage);


          // PostgreSQL의 ChatEntity에 마지막 메시지 정보 업데이트
          chatRepository.findById(chatRoomUid).ifPresent(chatEntity -> {
              chatEntity.setLastChat(messageContent);
              chatEntity.setLastChatSender(senderName); // 또는 senderId
              chatEntity.setUpdateTime(LocalDateTime.now()); // @UpdateTimestamp가 자동으로 처리
              chatRepository.save(chatEntity);
          });

          return savedMessage;
      }

      /**
       * 특정 채팅방을 삭제합니다.
       * 해당 채팅방의 모든 메시지도 함께 삭제됩니다.
       * @param chatRoomUid 삭제할 채팅방의 UID
       */
      @Transactional
      public void deleteChatRoom(UUID chatRoomUid) {
          // 1. MongoDB에서 해당 채팅방의 모든 메시지 삭제
          chatMessageRepository.deleteByChatRoomId(chatRoomUid.toString());

          // 2. PostgreSQL에서 ChatEntity 삭제
          chatRepository.deleteById(chatRoomUid);
      }
      
      @Transactional
      public ChatMessage saveFileMessage(UUID chatRoomUid, String senderId, String senderName, MultipartFile file) throws IOException {
          // 1. 파일 저장 (임시 로직, 실제로는 파일 시스템 또는 클라우드 스토리지에 저장)
          // 예시: 파일을 특정 디렉토리에 저장하고, 저장된 파일의 URL/경로를 메시지 내용으로 사용
          String fileUrl = "/uploads/" + file.getOriginalFilename(); // 임시 URL

          // 실제 파일 저장 로직 (예시)
          // Path uploadPath = Paths.get("D:/full_stack_lgh/5.springboot/edutalk/uploads"); // 실제 저장 경로
          // if (!Files.exists(uploadPath)) {
          //     Files.createDirectories(uploadPath);
          // }
          // Files.copy(file.getInputStream(), uploadPath.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);

          // 2. ChatMessage 생성 (파일 메시지 타입)
          ChatMessage chatMessage = new ChatMessage();
          chatMessage.setChatRoomId(chatRoomUid.toString());
          chatMessage.setSenderId(senderId);
          chatMessage.setSenderName(senderName);
          chatMessage.setMessage(fileUrl); // 파일 URL을 메시지 내용으로 저장
          chatMessage.setType("file"); // 메시지 타입을 "file"로 설정
          chatMessage.setTimestamp(LocalDateTime.now());

          // 3. MongoDB에 메시지 저장
          ChatMessage savedMessage = chatMessageRepository.save(chatMessage);

          // 4. PostgreSQL의 ChatEntity에 마지막 메시지 정보 업데이트
          chatRepository.findById(chatRoomUid).ifPresent(chatEntity -> {
              chatEntity.setLastChat("[파일] " + file.getOriginalFilename()); // 마지막 메시지를 파일 정보로 업데이트
              chatEntity.setLastChatSender(senderName);
              chatEntity.setUpdateTime(LocalDateTime.now());
              chatRepository.save(chatEntity);
          });

          return savedMessage;
      }

      
      // ChatEntity를 ChatRoomDto로 변환하는 헬퍼 메소드
      private ChatRoomDto convertToChatRoomDto(ChatEntity chatEntity, ChatMessage lastMessage) {
          ChatRoomDto dto = new ChatRoomDto();
          dto.setId(chatEntity.getUid());
          dto.setInstructorName(chatEntity.getInstructorName());
          dto.setStudentName(chatEntity.getStudentName());
          dto.setLastMessage(lastMessage != null ? lastMessage.getMessage() : null);
          dto.setLastMessageSender(lastMessage != null ? lastMessage.getSenderName() : null);
          dto.setLastMessageTime(lastMessage != null ? lastMessage.getTimestamp() : chatEntity.getUpdateTime());
          return dto;
      }
  }