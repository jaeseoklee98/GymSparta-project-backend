package com.sparta.gymspartaprojectbackend.notification.dto;

import com.sparta.gymspartaprojectbackend.notification.entity.AllNotification;
import lombok.Getter;

@Getter
public class NotificationDetailResponse {
  private Long AllNotificationId;
  private String title;
  private String message;
  private String image;

  public NotificationDetailResponse(AllNotification allNotification) {
    this.AllNotificationId = allNotification.getId();
    this.title = allNotification.getTitle();
    this.message = allNotification.getMessage();
    this.image = allNotification.getImage();
  }
}
