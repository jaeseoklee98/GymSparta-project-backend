package com.sparta.fitpleprojectbackend.trainer.dto;

import com.sparta.fitpleprojectbackend.trainer.entity.Trainer;
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
