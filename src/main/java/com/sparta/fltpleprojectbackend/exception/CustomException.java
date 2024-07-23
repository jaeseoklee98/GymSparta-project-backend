package com.sparta.fltpleprojectbackend.exception;

import com.sparta.fltpleprojectbackend.enums.ErrorType;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

  private String result;
  private ErrorType errorType;

  public CustomException(ErrorType errorType) {
    this.result = "ERROR";
    this.errorType = errorType;
  }
}