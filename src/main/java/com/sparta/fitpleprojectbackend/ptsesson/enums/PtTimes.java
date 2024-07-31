package com.sparta.fitpleprojectbackend.ptsesson.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum PtTimes {

  TEN_TIMES(10),
  TWENTY_TIMES(20),
  THIRTY_TIMES(30),
  SIXTY_TIMES(60);

  private final int times;

  PtTimes(int times) {
    this.times = times;
  }

  public int getTimes() {
    return times;
  }
}
