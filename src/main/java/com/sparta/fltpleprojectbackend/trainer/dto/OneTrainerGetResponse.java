package com.sparta.fltpleprojectbackend.trainer.dto;

import com.sparta.fltpleprojectbackend.trainer.entity.Trainer;
import lombok.Getter;

@Getter
public class OneTrainerGetResponse {
  private Long trainerId;
  private String trainerName;
  private String trainerInfo;
  private String trainerEmail;
  private String trainerPicture;
  private String trainerPhoneNumber;

  public OneTrainerGetResponse(Trainer trainer) {
    this.trainerId = trainer.getTrainerId();
    this.trainerName = trainer.getTrainerName();
    this.trainerInfo = trainer.getTrainerInfo();
    this.trainerEmail = trainer.getEmail();
    this.trainerPicture = trainer.getTrainerPicture();
    this.trainerPhoneNumber = trainer.getTrainerPhoneNumber();
  }
}
