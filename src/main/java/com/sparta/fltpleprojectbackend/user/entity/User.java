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
  public User() {}

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

  // Getters and Setters
  public Long getId() {
    return id;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getResidentRegistrationNumber() {
    return residentRegistrationNumber;
  }

  public void setResidentRegistrationNumber(String residentRegistrationNumber) {
    this.residentRegistrationNumber = residentRegistrationNumber;
  }

  public String getForeignerRegistrationNumber() {
    return foreignerRegistrationNumber;
  }

  public void setForeignerRegistrationNumber(String foreignerRegistrationNumber) {
    this.foreignerRegistrationNumber = foreignerRegistrationNumber;
  }

  public Boolean getIsForeigner() {
    return isForeigner;
  }

  public void setIsForeigner(Boolean isForeigner) {
    this.isForeigner = isForeigner;
  }

  public String getAccountId() {
    return accountId;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getUserPicture() {
    return userPicture;
  }

  public void setUserPicture(String userPicture) {
    this.userPicture = userPicture;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getZipcode() {
    return zipcode;
  }

  public void setZipcode(String zipcode) {
    this.zipcode = zipcode;
  }

  public String getMainAddress() {
    return mainAddress;
  }

  public void setMainAddress(String mainAddress) {
    this.mainAddress = mainAddress;
  }

  public String getDetailedAddress() {
    return detailedAddress;
  }

  public void setDetailedAddress(String detailedAddress) {
    this.detailedAddress = detailedAddress;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public LocalDateTime getDeletedAt() {
    return deletedAt;
  }

  public void setDeletedAt(LocalDateTime deletedAt) {
    this.deletedAt = deletedAt;
  }

  public LocalDateTime getScheduledDeletionDate() {
    return scheduledDeletionDate;
  }

  public void setScheduledDeletionDate(LocalDateTime scheduledDeletionDate) {
    this.scheduledDeletionDate = scheduledDeletionDate;
  }
}