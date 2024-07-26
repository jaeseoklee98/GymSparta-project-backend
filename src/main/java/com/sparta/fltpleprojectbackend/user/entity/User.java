package com.sparta.fltpleprojectbackend.user.entity;

import com.sparta.fltpleprojectbackend.enums.Role;
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
  private String userName;

  @Column(length = 13)
  private String residentRegistrationNumber;

  @Column(length = 13)
  private String foreignerRegistrationNumber;

  @Column(nullable = false)
  private Boolean isForeigner = false;

  @Column(nullable = false, length = 15, unique = true)
  private String accountId;

  @Column(nullable = false, length = 255)
  private String password;

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
}