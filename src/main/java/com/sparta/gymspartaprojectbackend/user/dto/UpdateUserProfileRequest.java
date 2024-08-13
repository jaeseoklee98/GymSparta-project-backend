package com.sparta.gymspartaprojectbackend.user.dto;

import lombok.Getter;

@Getter
public class UpdateUserProfileRequest {

  private String username;

  private String nickname;

  private String email;

  private String phoneNumber;

  private String zipcode;

  private String mainAddress;

  private String detailedAddress;

  private String userPicture;

  private String password;
}
