package com.sparta.gymspartaprojectbackend.trainer.repository;

import com.sparta.gymspartaprojectbackend.trainer.entity.Trainer;
import java.util.List;

public interface TrainerRepositoryQuery {
  List<Trainer> findAllTrainersByOwnerId(Long ownerId);
}
