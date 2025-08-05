package com.edutalk.edutalk.dto;

  import lombok.Getter;
  import lombok.Setter;


  @Getter
  @Setter
  public class CreateNoticeRequestDto {
      private String title;
      private String context;
      private String instructor;
      // 필요하다면 displayOrder, expirationTime 등도 추가할 수 있습니다.
  }