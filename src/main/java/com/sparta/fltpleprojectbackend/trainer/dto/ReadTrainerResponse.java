package com.sparta.fltpleprojectbackend.trainer.dto;

import com.sparta.fltpleprojectbackend.trainer.entity.Trainer;
import lombok.Getter;

@Getter
public class ReadTrainerResponse {
  private String trainerName;

  private String trainerInfo;

  private String nickname;

  private String email;

  private String trainerPicture;

  private String trainerPhoneNumber;

  public ReadTrainerResponse(Trainer trainer) {
    this.trainerName = trainer.getTrainerName();
    this.trainerInfo = trainer.getTrainerInfo();
    this.nickname = trainer.getNickname();
    this.email = trainer.getEmail();
    this.trainerPicture = trainer.getTrainerPicture();
    this.trainerPhoneNumber = trainer.getTrainerPhoneNumber();
  }
}
