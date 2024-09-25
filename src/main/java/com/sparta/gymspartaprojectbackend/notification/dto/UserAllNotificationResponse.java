package com.sparta.gymspartaprojectbackend.notification.dto;

import lombok.Getter;

@Getter
public class UserAllNotificationResponse {
  private String message;

  public UserAllNotificationResponse(String message) {
    this.message = message;
  }
}
