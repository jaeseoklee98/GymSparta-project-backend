package com.sparta.gymspartaprojectbackend.store.exception;

import com.sparta.gymspartaprojectbackend.enums.ErrorType;
import com.sparta.gymspartaprojectbackend.exception.CustomException;

public class StoreException extends CustomException {

  public StoreException(ErrorType errorType) {
    super(errorType);
  }

}
