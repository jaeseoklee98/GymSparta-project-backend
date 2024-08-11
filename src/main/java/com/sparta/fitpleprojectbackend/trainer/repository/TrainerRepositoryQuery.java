package com.sparta.fitpleprojectbackend.trainer.repository;

import com.sparta.fitpleprojectbackend.trainer.entity.Trainer;
import java.util.List;

public interface TrainerRepositoryQuery {
  List<Trainer> findAllTrainersByOwnerId(Long ownerId);
}
