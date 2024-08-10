package com.sparta.fltpleprojectbackend.trainer.dto;

import com.sparta.fltpleprojectbackend.trainer.entity.Trainer;
import lombok.Getter;

@Getter
public class TrainerGetResponse {
  private String trainerName;
  private String trainerPicture;

  public TrainerGetResponse(Trainer trainer) {
    this.trainerName = trainer.getTrainerName();
    this.trainerPicture = trainer.getTrainerPicture();
  }
}
