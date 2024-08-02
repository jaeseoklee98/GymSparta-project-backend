package com.sparta.fitpleprojectbackend.ptsesson.dto;

import com.sparta.fitpleprojectbackend.trainer.entity.Trainer;
import com.sparta.fitpleprojectbackend.user.entity.User;
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
