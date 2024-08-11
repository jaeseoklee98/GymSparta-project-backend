package com.sparta.fitpleprojectbackend.store.exception;

import com.sparta.fitpleprojectbackend.enums.ErrorType;
import com.sparta.fitpleprojectbackend.exception.CustomException;

public class StoreException extends CustomException {

  public StoreException(ErrorType errorType) {
    super(errorType);
  }

}
