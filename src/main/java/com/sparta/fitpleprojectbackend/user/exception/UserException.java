package com.sparta.fitpleprojectbackend.user.exception;

import com.sparta.fitpleprojectbackend.enums.ErrorType;
import com.sparta.fitpleprojectbackend.exception.CustomException;

public class UserException extends CustomException {

  public UserException(ErrorType errorType) {

    super(errorType);
  }
}