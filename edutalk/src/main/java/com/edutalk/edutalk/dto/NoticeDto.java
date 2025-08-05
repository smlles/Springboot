package com.edutalk.edutalk.dto;

  import lombok.Getter;
  import lombok.Setter;

  import java.time.LocalDateTime;
  import java.util.UUID;


  @Getter
  @Setter
  public class NoticeDto {
      private UUID id; // NoticeEntity의 uid
      private String title;
      private String content;
      private String author; // instructor 필드를 author로 매핑
      private LocalDateTime createdAt;
      private int displayOrder;
  }