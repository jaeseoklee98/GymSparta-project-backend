package com.sparta.gymspartaprojectbackend.notification.entity;

import com.sparta.gymspartaprojectbackend.common.TimeStamped;
import com.sparta.gymspartaprojectbackend.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class UserAllNotification extends TimeStamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String title;

  private String message;

  private boolean deleted = false;

  @ManyToOne
  @JoinColumn(name = "allNotification_id")
  private AllNotification allNotification;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  public UserAllNotification(String title, String message,  User user, AllNotification allNotification) {
    this.title = title;
    this.message = message;
    this.allNotification = allNotification;
    this.user = user;
  }
}
