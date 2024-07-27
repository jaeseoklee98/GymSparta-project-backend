package com.sparta.fltpleprojectbackend.trainer.exception;

import com.sparta.fltpleprojectbackend.enums.ErrorType;
import com.sparta.fltpleprojectbackend.exception.CustomException;

public class TrainerException extends CustomException {

  public TrainerException(ErrorType errorType) {
    super(errorType, "Trainer Exception");
  }
}
