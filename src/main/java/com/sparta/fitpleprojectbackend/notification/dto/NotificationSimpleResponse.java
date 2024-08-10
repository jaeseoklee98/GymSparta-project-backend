package com.sparta.fitpleprojectbackend.notification.dto;

import com.sparta.fitpleprojectbackend.notification.entity.AllNotification;
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
