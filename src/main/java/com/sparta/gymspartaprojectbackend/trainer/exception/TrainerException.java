package com.sparta.gymspartaprojectbackend.trainer.exception;

import com.sparta.gymspartaprojectbackend.enums.ErrorType;
import com.sparta.gymspartaprojectbackend.exception.CustomException;

public class TrainerException extends CustomException {

  public TrainerException(ErrorType errorType) {
    super(errorType);
  }
}