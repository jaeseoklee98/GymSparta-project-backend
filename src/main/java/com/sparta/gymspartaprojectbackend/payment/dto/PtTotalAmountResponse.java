package com.sparta.gymspartaprojectbackend.payment.dto;

import lombok.Getter;

@Getter
public class PtTotalAmountResponse {

  private final String ptTimes;

  private final int times;

  private final double amount;

  public PtTotalAmountResponse(String ptTimes, int times, double amount) {
    this.ptTimes = ptTimes;
    this.times = times;
    this.amount = amount;
  }
}