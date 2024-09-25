package com.sparta.gymspartaprojectbackend.user.exception;

import com.sparta.gymspartaprojectbackend.enums.ErrorType;
import com.sparta.gymspartaprojectbackend.exception.CustomException;

public class UserException extends CustomException {

  public UserException(ErrorType errorType) {

    super(errorType);
  }
}