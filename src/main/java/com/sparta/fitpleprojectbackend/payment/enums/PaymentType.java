package com.sparta.fitpleprojectbackend.payment.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum PaymentType {
  UNDEFINED("Undefined"),
  CREDIT_CARD("Credit Card"),
  DEBIT_CARD("Debit Card"),
  CASH("Cash");

  private final String Types;

  PaymentType(String Types) {
    this.Types = Types;
  }

  public String getTypes() {
    return Types;
  }
}