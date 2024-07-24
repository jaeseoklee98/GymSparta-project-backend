package com.sparta.fltpleprojectbackend.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignupRequest {
  private String userName;  // 유저 이름 (휴대폰 인증 후 얻은 이름)
  private String accountId;  // 아이디
  private String password;
  private String confirmPassword;
  private String email;
  private String phoneNumber;  // 전화번호 추가
  private String residentRegistrationNumber; // 주민등록번호 추가
  private String foreignerRegistrationNumber; // 외국인등록번호 추가
}