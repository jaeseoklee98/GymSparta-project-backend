package com.sparta.fitpleprojectbackend.trainer.dto;

import com.sparta.fitpleprojectbackend.trainer.entity.Trainer;
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
