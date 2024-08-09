package com.sparta.fitpleprojectbackend.user.entity;

import com.sparta.fitpleprojectbackend.common.TimeStamped;
import com.sparta.fitpleprojectbackend.enums.Role;
import com.sparta.fitpleprojectbackend.notification.entity.UserAllNotification;
import com.sparta.fitpleprojectbackend.user.dto.UpdateUserProfileRequest;
import jakarta.persistence.*;
import java.util.List;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
public class User extends TimeStamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private Double balance; // 잔고 테스트용

  @Column(nullable = false, length = 10)
  private String userName; //x

  @Column(length = 13)
  private String residentRegistrationNumber; //x

  @Column(length = 13)
  private String foreignerRegistrationNumber; //x

  @Column(nullable = false)
  private Boolean isForeigner = false; //x

  @Column(nullable = false, length = 15, unique = true)
  private String accountId; //x

  @Column(nullable = false)
  private String password; //0

  @Column(length = 10)
  private String nickname = ""; //0

  @Column(nullable = false, length = 255)
  private String email; //x

  @Column(length = 255)
  private String userPicture = ""; //0

  @Column(nullable = false, length = 10)
  private String status = "ACTIVE";

  @Column(length = 10)
  private String zipcode = ""; //0

  @Column(length = 255)
  private String mainAddress = ""; //0

  @Column(length = 255)
  private String detailedAddress = ""; //0

  @Column(length = 15)
  private String phoneNumber = ""; //x

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role;

  @OneToMany(mappedBy = "user")
  private List<UserAllNotification> userAllNotificationList;

  @Column
  private LocalDateTime deletedAt;

  @Column
  private LocalDateTime scheduledDeletionDate;

  public User() {
  }

  public User(String userName, Double balance, String residentRegistrationNumber,
      String foreignerRegistrationNumber, Boolean isForeigner,
      String accountId, String password, String nickname, String email, String userPicture,
      String status,
      String zipcode, String mainAddress, String detailedAddress, String phoneNumber, Role role,
      LocalDateTime deletedAt, LocalDateTime scheduledDeletionDate) {
    this.userName = userName;
    this.balance = balance;
    this.residentRegistrationNumber = residentRegistrationNumber;
    this.foreignerRegistrationNumber = foreignerRegistrationNumber;
    this.isForeigner = isForeigner != null ? isForeigner : false;
    this.accountId = accountId;
    this.password = password;
    this.nickname = nickname != null ? nickname : "";
    this.email = email;
    this.userPicture = userPicture != null ? userPicture : "";
    this.status = status != null ? status : "ACTIVE";
    this.zipcode = zipcode != null ? zipcode : "";
    this.mainAddress = mainAddress != null ? mainAddress : "";
    this.detailedAddress = detailedAddress != null ? detailedAddress : "";
    this.phoneNumber = phoneNumber != null ? phoneNumber : "";
    this.role = role;
    this.deletedAt = deletedAt;
    this.scheduledDeletionDate = scheduledDeletionDate;
  }

  /**
   * . 비밀번호 변경
   *
   * @param newPassword 새 비밀번호 정보
   */
  public void updatePassword(String newPassword) {
    this.password = newPassword;
  }

  /**
   * . 프로필 변경
   *
   * @param userRequest 새 프로필 정보
   */
  public void updateUserProfile(UpdateUserProfileRequest userRequest) {
    this.nickname = userRequest.getNickname();
    this.zipcode = userRequest.getZipcode();
    this.mainAddress = userRequest.getMainAddress();
    this.detailedAddress = userRequest.getDetailedAddress();
    this.userPicture = userRequest.getUserPicture();
  }
}