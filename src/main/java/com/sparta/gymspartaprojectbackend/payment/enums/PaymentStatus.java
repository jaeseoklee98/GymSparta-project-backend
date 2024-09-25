package com.sparta.gymspartaprojectbackend.payment.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum PaymentStatus {
  PENDING,
  COMPLETED,
  FAILED,
  CANCELED,
  APPROVED
}
