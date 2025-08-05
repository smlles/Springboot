package com.edutalk.edutalk.model;


  import lombok.Getter;
  import lombok.Setter;
  import org.springframework.data.annotation.Id;
  import org.springframework.data.mongodb.core.mapping.Document;
  import org.springframework.data.mongodb.core.mapping.Field;

  import java.time.LocalDateTime;


  @Document(collection = "chat_messages") // MongoDB의 컬렉션 이름을 지정     
  @Getter
  @Setter
  public class ChatMessage {

      @Id
      private String id; // MongoDB의 기본 키(_id)는 보통 String 타입으로 매핑


      @Field("chat_room_id") // 실제 MongoDB 필드 이름
      private String chatRoomId; // ChatEntity의 uid (UUID)를 String으로 저장

      @Field("sender_id")
      private String senderId;

      @Field("sender_name")
      private String senderName;

      @Field("message")
      private String message;
      
      @Field("type")
      private String type;

      @Field("timestamp")
      private LocalDateTime timestamp;
  }