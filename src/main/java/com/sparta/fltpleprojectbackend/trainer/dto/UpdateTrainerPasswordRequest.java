package com.sparta.fltpleprojectbackend.trainer.dto;

import lombok.Getter;

@Getter
public class UpdateTrainerPasswordRequest {
  private String oldPassword;
  private String newPassword;
}
