package com.sparta.fltpleprojectbackend.user.dto;

import lombok.Getter;

@Getter
public class UpdatePasswordRequest {
  private String oldPassword;
  private String newPassword;
}
