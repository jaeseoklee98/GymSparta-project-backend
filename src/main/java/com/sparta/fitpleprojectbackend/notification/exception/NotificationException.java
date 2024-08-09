package com.sparta.fitpleprojectbackend.notification.exception;

import com.sparta.fitpleprojectbackend.enums.ErrorType;
import com.sparta.fitpleprojectbackend.exception.CustomException;

public class NotificationException extends CustomException {

  public NotificationException(ErrorType errorType) {
    super(errorType);
  }
}
