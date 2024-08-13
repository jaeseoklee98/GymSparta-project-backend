package com.sparta.gymspartaprojectbackend.owner.exception;

import com.sparta.gymspartaprojectbackend.enums.ErrorType;
import com.sparta.gymspartaprojectbackend.exception.CustomException;

public class OwnerException extends CustomException {

  public OwnerException(ErrorType errorType) {
    super(errorType);
  }
}
