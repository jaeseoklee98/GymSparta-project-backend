package com.sparta.fitpleprojectbackend.ptsesson.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.fitpleprojectbackend.ptsesson.enums.PtTimes;
import lombok.Getter;

@Getter
public class PtInformationRequest {

  private final Long trainerId;

  private final Long userId;

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private final PtTimes ptTimes;

  public PtInformationRequest(Long trainerId, Long userId, PtTimes ptTimes) {
    this.trainerId = trainerId;
    this.userId = userId;
    this.ptTimes = ptTimes;
  }

}

