package com.sparta.fitpleprojectbackend.payment.dto;

import lombok.Getter;

@Getter
public class UserPtRequest {

  private final Long trainerId;

  private final Long userId;

  public UserPtRequest (Long trainerId, Long userId){
    this.trainerId = trainerId;
    this.userId = userId;
  }
}
