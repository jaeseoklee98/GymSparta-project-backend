package com.sparta.fltpleprojectbackend.user.dto;

import lombok.Data;

@Data
public class UserSignupRequest {
  private String username;
  private String password;
  private String confirmPassword;
  private String email;
  private String phoneNumber;
  private String name;
}