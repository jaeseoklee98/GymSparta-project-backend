package com.sparta.fltpleprojectbackend.owner.exception;

import com.sparta.fltpleprojectbackend.enums.ErrorType;
import com.sparta.fltpleprojectbackend.exception.CustomException;

public class OwnerException extends CustomException {

  public OwnerException(ErrorType errorType) {
    super(errorType, "Owner Exception");
  }
}
