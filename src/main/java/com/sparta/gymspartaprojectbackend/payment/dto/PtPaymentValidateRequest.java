package com.sparta.gymspartaprojectbackend.payment.dto;

import com.sparta.gymspartaprojectbackend.trainer.entity.Trainer;
import com.sparta.gymspartaprojectbackend.user.entity.User;
import lombok.Getter;

@Getter
public class PtPaymentValidateRequest {

  private final Trainer trainer;

  private final User user;

  public PtPaymentValidateRequest(Trainer trainer, User user) {
    this.trainer = trainer;
    this.user = user;
  }
}
