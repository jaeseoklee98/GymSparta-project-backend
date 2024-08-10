package com.sparta.fitpleprojectbackend.owner.dto;

import lombok.Getter;

@Getter
public class UpdateOwnerPasswordRequest {

  private String oldPassword;

  private String newPassword;
}
