package com.sparta.gymspartaprojectbackend.owner.dto;

import com.sparta.gymspartaprojectbackend.owner.entity.Owner;
import lombok.Getter;

@Getter
public class ReadOwnerResponse {

  private String ownerName;

  private String accountId;

  private String nickname;

  private String email;

  private String ownerPicture;

  private String zipcode;

  private String mainAddress;

  private String detailedAddress;

  private String ownerPhoneNumber;

  public ReadOwnerResponse(Owner owner) {
    this.ownerName = owner.getOwnerName();
    this.nickname = owner.getNickname();
    this.email = owner.getEmail();
    this.ownerPicture = owner.getOwnerPicture();
    this.zipcode = owner.getZipcode();
    this.mainAddress = owner.getMainAddress();
    this.detailedAddress = owner.getDetailedAddress();
    this.ownerPhoneNumber = owner.getOwnerPhoneNumber();
  }
}
