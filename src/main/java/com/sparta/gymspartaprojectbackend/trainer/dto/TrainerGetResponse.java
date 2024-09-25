package com.sparta.gymspartaprojectbackend.trainer.dto;

import com.sparta.gymspartaprojectbackend.trainer.entity.Trainer;
import lombok.Getter;

@Getter
public class TrainerGetResponse {

  private Long id;

  private String name;

  private String trainerPicture;

  public TrainerGetResponse(Trainer trainer) {
    this.id = trainer.getId();
    this.name = trainer.getTrainerName();
    this.trainerPicture = trainer.getTrainerPicture();
  }
}
