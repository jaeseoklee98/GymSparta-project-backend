package com.sparta.gymspartaprojectbackend.user.entity;

import com.sparta.gymspartaprojectbackend.common.TimeStamped;
import com.sparta.gymspartaprojectbackend.enums.Role;
import com.sparta.gymspartaprojectbackend.notification.entity.PaymentUserNotification;
import com.sparta.gymspartaprojectbackend.notification.entity.UserAllNotification;
import com.sparta.gymspartaprojectbackend.notification.entity.UserNotification;
import com.sparta.gymspartaprojectbackend.user.dto.UpdateUserProfileRequest;
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

  @Getter
  @Column(nullable = false, length = 50)
  private String userName;

  @Column(length = 13)
  private String residentRegistrationNumber;

  @Column(length = 13)
  private String foreignerRegistrationNumber;

  @Column(nullable = false)
  private Boolean isForeigner = false;

  @Getter
  @Column(nullable = false, length = 15, unique = true)
  private String accountId;

  @Column(nullable = false)
  private String password;

  @Getter
  @Column(length = 10)
  private String nickname = "";

  @Column(nullable = false, length = 255)
  private String email;

  @Column(length = 255)
  private String userPicture = "";

  @Column(nullable = false, length = 10)
  private String status = "ACTIVE";

  @Column(length = 10)
  private String zipcode = "";

  @Column(length = 255)
  private String mainAddress = "";

  @Column(length = 255)
  private String detailedAddress = "";

  @Column(length = 15)
  private String phoneNumber = "";

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role;

  @OneToMany(mappedBy = "user")
  private List<UserAllNotification> userAllNotificationList;

  @OneToMany(mappedBy = "user")
  private List<UserNotification> userNotificationList;

  @OneToMany(mappedBy = "user")
  private List<PaymentUserNotification> paymentUserNotificationList;

  @Column
  private LocalDateTime membershipExpiryDate;

  @Column
  private LocalDateTime deletedAt;

  @Column
  private LocalDateTime scheduledDeletionDate;


  public User() {
  }

  public User(String userName, String residentRegistrationNumber,
      String foreignerRegistrationNumber, Boolean isForeigner,
      String accountId, String password, String nickname, String email, String userPicture,
      String status,
      String zipcode, String mainAddress, String detailedAddress, String phoneNumber, Role role,
      LocalDateTime deletedAt, LocalDateTime scheduledDeletionDate) {
    this.userName = userName;
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
    this.email = userRequest.getEmail();
    this.phoneNumber = userRequest.getPhoneNumber();
    this.userName = userRequest.getUsername();
  }

  public void activateMembership(LocalDateTime expiryDate) {
    this.membershipExpiryDate = expiryDate;
  }

  public void deactivateMembership() {
    this.membershipExpiryDate = null;
  }

  public boolean isMembershipActive() {
    return membershipExpiryDate != null && membershipExpiryDate.isAfter(LocalDateTime.now());
  }
}