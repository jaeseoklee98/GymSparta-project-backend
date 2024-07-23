package com.sparta.fltpleprojectbackend.user.exception;

import com.sparta.fltpleprojectbackend.enums.ErrorType;
import com.sparta.fltpleprojectbackend.exception.CustomException;

public class UserException extends CustomException {

  public UserException(ErrorType errorType) {
    super(errorType);
  }
}
