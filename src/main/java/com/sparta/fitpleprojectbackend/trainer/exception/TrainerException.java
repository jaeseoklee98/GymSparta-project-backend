package com.sparta.fitpleprojectbackend.trainer.exception;

import com.sparta.fitpleprojectbackend.enums.ErrorType;
import com.sparta.fitpleprojectbackend.exception.CustomException;

public class TrainerException extends CustomException {

  public TrainerException(ErrorType errorType) {
    super(errorType);
  }
}