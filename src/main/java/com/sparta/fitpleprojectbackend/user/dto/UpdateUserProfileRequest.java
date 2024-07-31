package com.sparta.fitpleprojectbackend.user.dto;

import lombok.Getter;

@Getter
public class UpdateUserProfileRequest {

  private String nickname;

  private String zipcode;

  private String mainAddress;

  private String detailedAddress;

  private String userPicture;

  private String password;
}
