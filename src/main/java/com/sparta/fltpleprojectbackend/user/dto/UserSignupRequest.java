package com.sparta.fltpleprojectbackend.user.dto;

public class UserSignupRequest {

  private String username;
  private String password;
  private String confirmPassword;
  private String userName;
  private String residentRegistrationNumber;
  private String foreignerRegistrationNumber;
  private Boolean isForeigner;
  private String nickname;
  private String email;
  private String userPicture;
  private String status;
  private String zipcode;
  private String mainAddress;
  private String detailedAddress;
  private String phoneNumber;

  // Getters and Setters
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getConfirmPassword() {
    return confirmPassword;
  }

  public void setConfirmPassword(String confirmPassword) {
    this.confirmPassword = confirmPassword;
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
}