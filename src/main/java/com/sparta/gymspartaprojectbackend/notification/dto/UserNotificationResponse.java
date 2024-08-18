package com.sparta.gymspartaprojectbackend.notification.dto;

import lombok.Getter;

@Getter
public class UserNotificationResponse {
  private String title;
  private String message;

  public UserNotificationResponse(String title, String message) {
    this.title = title;
    this.message = message;
  }
}
