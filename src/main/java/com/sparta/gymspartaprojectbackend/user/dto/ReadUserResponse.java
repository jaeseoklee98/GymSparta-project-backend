package com.sparta.gymspartaprojectbackend.user.dto;

import com.sparta.gymspartaprojectbackend.owner.entity.Owner;
import com.sparta.gymspartaprojectbackend.user.entity.User;
import com.sparta.gymspartaprojectbackend.trainer.entity.Trainer; // Trainer 클래스 import
import lombok.Getter;

@Getter
public class ReadUserResponse {
  private String userName;

  private String accountId;

  private String nickname;

  private String email;

  private String userPicture;

  private String zipcode;

  private String mainAddress;

  private String detailedAddress;

  private String phoneNumber;

  private String role; // role 필드 추가

  public ReadUserResponse(User user) {
    this.userName = user.getUserName();
    this.accountId = user.getAccountId();
    this.nickname = user.getNickname();
    this.email = user.getEmail();
    this.userPicture = user.getUserPicture();
    this.zipcode = user.getZipcode();
    this.mainAddress = user.getMainAddress();
    this.detailedAddress = user.getDetailedAddress();
    this.phoneNumber = user.getPhoneNumber();
    this.role = user.getRole().name();
  }

  public ReadUserResponse(Owner owner) {
    this.userName = owner.getOwnerName();
    this.accountId = owner.getAccountId();
    this.nickname = owner.getNickname();
    this.email = owner.getEmail();
    this.userPicture = owner.getOwnerPicture();
    this.zipcode = owner.getZipcode();
    this.mainAddress = owner.getMainAddress();
    this.detailedAddress = owner.getDetailedAddress();
    this.phoneNumber = owner.getOwnerPhoneNumber();
    this.role = owner.getRole().name();
  }

  public ReadUserResponse(Trainer trainer) {
    this.userName = trainer.getTrainerName();
    this.accountId = trainer.getAccountId();
    this.nickname = trainer.getNickname();
    this.email = trainer.getEmail();
    this.userPicture = trainer.getTrainerPicture();
    this.phoneNumber = trainer.getTrainerPhoneNumber();
    this.role = trainer.getRole().name();
  }
}