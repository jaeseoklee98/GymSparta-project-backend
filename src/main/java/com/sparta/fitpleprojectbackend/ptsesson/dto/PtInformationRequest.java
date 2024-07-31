package com.sparta.fitpleprojectbackend.ptsesson.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.fitpleprojectbackend.ptsesson.enums.PtTimes;
import lombok.Getter;

@Getter
public class PtInformationRequest {

  private Long trainerId;

  private Long userId;

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private PtTimes ptTimes;

}

