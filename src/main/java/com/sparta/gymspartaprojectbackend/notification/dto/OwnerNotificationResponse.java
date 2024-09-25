package com.sparta.gymspartaprojectbackend.notification.dto;

import lombok.Getter;

@Getter
public class OwnerNotificationResponse {
  private String title;
  private String message;

  public OwnerNotificationResponse(String title, String message) {
    this.title = title;
    this.message = message;
  }
}
