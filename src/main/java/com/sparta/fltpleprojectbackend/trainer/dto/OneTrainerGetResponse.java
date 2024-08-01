package com.sparta.fltpleprojectbackend.trainer.dto;

import com.sparta.fltpleprojectbackend.trainer.entity.Trainer;
import lombok.Getter;

@Getter
public class OneTrainerGetResponse {
  private Long id;
  private String name;
  private String introduction;
  private String trainerEmail;
  private String image;
  private String trainerPhoneNumber;

  public OneTrainerGetResponse(Trainer trainer) {
    this.id = trainer.getTrainerId();
    this.name = trainer.getTrainerName();
    this.introduction = trainer.getTrainerInfo();
    this.trainerEmail = trainer.getEmail();
    this.image = trainer.getTrainerPicture();
    this.trainerPhoneNumber = trainer.getTrainerPhoneNumber();
  }
}
