package com.sparta.fitpleprojectbackend.notification.entity;

import com.sparta.fitpleprojectbackend.common.TimeStamped;
import com.sparta.fitpleprojectbackend.user.entity.User;
import jakarta.persistence.*;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class UserAllNotification extends TimeStamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String message;

  private boolean deleted = false;

  @ManyToOne
  @JoinColumn(name = "allNotification_id")
  private AllNotification allNotification;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  public UserAllNotification(String message,  User user, AllNotification allNotification) {
    this.message = message;
    this.allNotification = allNotification;
    this.user = user;
  }
}
