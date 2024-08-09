package com.sparta.fitpleprojectbackend.payment.dto;

import lombok.Getter;

@Getter
public class PtTotalAmountRequest {

  private final String selectedTimes;

  private final double trainerPrice;

  public PtTotalAmountRequest(String selectedTimes, double trainerPrice) {
    this.selectedTimes = selectedTimes;
    this.trainerPrice = trainerPrice;
  }

}
