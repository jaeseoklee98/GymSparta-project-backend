package com.sparta.fltpleprojectbackend.user.dto;

import lombok.Data;

@Data
public class LoginRequest {
  private String accountId;
  private String password;
}