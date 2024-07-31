package com.sparta.fltpleprojectbackend.trainer.dto;

import lombok.Getter;

@Getter
public class UpdateTrainerProfileRequest {
  private String nickname;

  private String trainerInfo;

  private String trainerPicture;

  private String trainerPhoneNumber;

  private String email;

  private String password;
}
