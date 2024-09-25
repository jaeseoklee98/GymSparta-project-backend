package com.sparta.gymspartaprojectbackend.trainer.dto;

import com.sparta.gymspartaprojectbackend.trainer.entity.Trainer;
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
    this.id = trainer.getId();
    this.name = trainer.getTrainerName();
    this.introduction = trainer.getTrainerInfo();
    this.trainerEmail = trainer.getEmail();
    this.image = trainer.getTrainerPicture();
    this.trainerPhoneNumber = trainer.getTrainerPhoneNumber();
  }
}
