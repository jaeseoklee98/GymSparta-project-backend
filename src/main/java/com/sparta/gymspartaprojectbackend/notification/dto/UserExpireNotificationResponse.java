package com.sparta.gymspartaprojectbackend.notification.dto;

import lombok.Getter;

@Getter
public class UserExpireNotificationResponse {
  private String title;
  private String message;

  public UserExpireNotificationResponse(String title, String message) {
    this.title = title;
    this.message = message;
  }
}
