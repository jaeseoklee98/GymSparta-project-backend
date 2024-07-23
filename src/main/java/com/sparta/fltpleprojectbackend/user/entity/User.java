package com.sparta.fltpleprojectbackend.user.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 10)
  private String userName;

  @Column(length = 13)
  private String residentRegistrationNumber;

  @Column(length = 13)
  private String foreignerRegistrationNumber;

  @Column(nullable = false)
  private Boolean isForeigner;

  @Column(nullable = false, length = 15, unique = true)
  private String accountId;

  @Column(nullable = false, length = 255)
  private String password;

  @Column(nullable = false, length = 10)
  private String nickname;

  @Column(nullable = false, length = 255)
  private String email;

  @Column(length = 255)
  private String userPicture;

  @Column(nullable = false, length = 10)
  private String status;

  @Column(nullable = false, length = 10)
  private String zipcode;

  @Column(nullable = false, length = 255)
  private String mainAddress;

  @Column(nullable = false, length = 255)
  private String detailedAddress;

  @Column(nullable = false, length = 15)
  private String phoneNumber;

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
  public User() {
  }

  // 모든 필드를 포함한 생성자
  public User(String userName, String residentRegistrationNumber, String foreignerRegistrationNumber, Boolean isForeigner,
      String accountId, String password, String nickname, String email, String userPicture, String status,
      String zipcode, String mainAddress, String detailedAddress, String phoneNumber, Role role,
      LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt, LocalDateTime scheduledDeletionDate) {
    this.userName = userName;
    this.residentRegistrationNumber = residentRegistrationNumber;
    this.foreignerRegistrationNumber = foreignerRegistrationNumber;
    this.isForeigner = isForeigner;
    this.accountId = accountId;
    this.password = password;
    this.nickname = nickname;
    this.email = email;
    this.userPicture = userPicture;
    this.status = status;
    this.zipcode = zipcode;
    this.mainAddress = mainAddress;
    this.detailedAddress = detailedAddress;
    this.phoneNumber = phoneNumber;
    this.role = role;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.deletedAt = deletedAt;
    this.scheduledDeletionDate = scheduledDeletionDate;
  }
}