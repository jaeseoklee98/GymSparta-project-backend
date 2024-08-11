package com.sparta.fitpleprojectbackend.owner.exception;

import com.sparta.fitpleprojectbackend.enums.ErrorType;
import com.sparta.fitpleprojectbackend.exception.CustomException;

public class OwnerException extends CustomException {

  public OwnerException(ErrorType errorType) {
    super(errorType);
  }
}
