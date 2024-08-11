package com.sparta.fitpleprojectbackend.trainer.dto;

import lombok.Getter;

@Getter
public class UpdateTrainerProfileRequest {
  private String trainerName;

  private String nickname;

  private String trainerInfo;

  private String trainerPicture;

  private String trainerPhoneNumber;

  private String email;

  private String oldPassword;

  private String password;
}
