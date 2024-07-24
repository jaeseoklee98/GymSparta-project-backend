package com.sparta.fltpleprojectbackend.store.exception;

import com.sparta.fltpleprojectbackend.enums.ErrorType;
import com.sparta.fltpleprojectbackend.exception.CustomException;

public class StoreException extends CustomException {

  public StoreException(ErrorType errorType) {
    super(errorType);
  }

}
