 package com.edutalk.edutalk.dto;

  import lombok.Getter;
  import lombok.Setter;


  @Getter
  @Setter
  public class RegisterRequestDto {
      private String identy;
      private String email;
      private String password;
      // 회원가입 시 필요한 다른 정보가 있다면 여기에 추가할 수 있습니다.
  }