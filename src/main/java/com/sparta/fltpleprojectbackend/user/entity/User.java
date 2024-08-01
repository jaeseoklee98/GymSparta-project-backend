package com.sparta.fltpleprojectbackend.user.entity;

import com.sparta.fltpleprojectbackend.enums.Role;
import com.sparta.fltpleprojectbackend.user.dto.UpdateUserProfileRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

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

  @Column(nullable = false)
  private LocalDateTime createdAt;

  @Column
  private LocalDateTime updatedAt;

  @Column
  private LocalDateTime deletedAt;

  @Column
  private LocalDateTime scheduledDeletionDate;

  // 기본 생성자
  public User() {}

  // 모든 필드를 포함하는 생성자
  public User(String userName, String residentRegistrationNumber, String foreignerRegistrationNumber, Boolean isForeigner,
      String accountId, String password, String nickname, String email, String userPicture, String status,
      String zipcode, String mainAddress, String detailedAddress, String phoneNumber, Role role,
      LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt, LocalDateTime scheduledDeletionDate) {
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
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.deletedAt = deletedAt;
    this.scheduledDeletionDate = scheduledDeletionDate;
  }

  /**.
   * 비밀번호 변경
   *
   * @param newPassword 새 비밀번호 정보
   */
  public void updatePassword(String newPassword) {
    this.password = newPassword;
    this.updatedAt = LocalDateTime.now();
  }

  /**.
   * 프로필 변경
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
    this.updatedAt = LocalDateTime.now();
  }
}