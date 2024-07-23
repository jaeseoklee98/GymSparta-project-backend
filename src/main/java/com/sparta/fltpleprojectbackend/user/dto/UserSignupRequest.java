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
}