package com.sparta.fltpleprojectbackend.trainer.repository;

import com.sparta.fltpleprojectbackend.trainer.entity.Trainer;
import java.util.List;

public interface TrainerRepositoryQuery {
  List<Trainer> findAllTrainersByOwnerId(Long ownerId);
}
