 package com.edutalk.edutalk.dto;

  import lombok.Getter;
  import lombok.Setter;

  import java.time.LocalDateTime;
  import java.util.UUID;


  @Getter
  @Setter
  public class ChatRoomDto {
      private UUID id; // ChatEntity의 uid  
      private String instructorName;        
      private String studentName;
      private String lastMessage;
      private String lastMessageSender;     
      private LocalDateTime lastMessageTime;
  }