package com.sparta.gymspartaprojectbackend.notification.exception;

import com.sparta.gymspartaprojectbackend.enums.ErrorType;
import com.sparta.gymspartaprojectbackend.exception.CustomException;

public class NotificationException extends CustomException {

  public NotificationException(ErrorType errorType) {
    super(errorType);
  }
}
