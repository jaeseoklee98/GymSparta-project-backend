package com.sparta.fitpleprojectbackend.ptsesson.dto;

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
