package com.sparta.fltpleprojectbackend.owner.dto;

import lombok.Getter;

@Getter
public class UpdateOwnerProfileRequest {

  private String nickname;

  private String email;

  private String ownerPicture;

  private String zipcode;

  private String mainAddress;

  private String detailedAddress;

  private String ownerPhoneNumber;

  private String password;
}
