package com.sparta.gymspartaprojectbackend.notification.entity;

import com.sparta.gymspartaprojectbackend.common.TimeStamped;
import com.sparta.gymspartaprojectbackend.notification.dto.createAllNotificationDto;
import com.sparta.gymspartaprojectbackend.store.entity.Store;
import jakarta.persistence.*;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor
public class AllNotification extends TimeStamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column
  private String title;

  @Column
  private String message;

  @Column
  private String image;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "store_id")
  private Store store;

  @OneToMany(mappedBy = "allNotification")
  private List<UserAllNotification> userAllNotificationList;

  public AllNotification(createAllNotificationDto request, Store store) {
    this.title = request.getNoticeTitle();
    this.message = request.getNoticeContent();
    this.image = request.getImage();
    this.store = store;
  }
}
