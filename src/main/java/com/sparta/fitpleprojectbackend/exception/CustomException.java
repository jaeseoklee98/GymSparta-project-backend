package com.sparta.fitpleprojectbackend.exception;

import com.sparta.fitpleprojectbackend.enums.ErrorType;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

  private final String result;
  private final ErrorType errorType;
  private final String message;

  public CustomException(ErrorType errorType) {
    super(errorType.getMessage());
    this.result = "ERROR";
    this.errorType = errorType;
    this.message = errorType.getMessage();
  }

  public CustomException(ErrorType errorType, String message) {
    super(message);
    this.result = "ERROR";
    this.errorType = errorType;
    this.message = message;
  }
}