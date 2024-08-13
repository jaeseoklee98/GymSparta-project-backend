package com.sparta.gymspartaprojectbackend.notification.dto;

import com.sparta.gymspartaprojectbackend.notification.entity.AllNotification;
import lombok.Getter;

@Getter
public class NotificationSimpleResponse {
  private Long allNotificationId;
  private String title;

  public NotificationSimpleResponse(AllNotification allNotification) {
    this.allNotificationId = allNotification.getId();
    this.title = allNotification.getTitle();
  }
}
