package com.sparta.fitpleprojectbackend.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserSignupRequest {

  @NotBlank(message = "유저 이름은 필수 항목입니다.")
  @Size(max = 50, message = "유저 이름은 최대 50자까지 가능합니다.")
  private String userName;  // 유저 이름 (휴대폰 인증 후 얻은 이름)

  @NotNull(message = "잔고를 입력해 주세요")
  private Double balance;  // 잔고

  @NotBlank(message = "아이디는 필수 항목입니다.")
  @Size(max = 15, message = "아이디는 최대 15자까지 가능합니다.")
  private String accountId;  // 아이디

  @NotBlank(message = "비밀번호는 필수 항목입니다.")
  @Size(min = 8, max = 255, message = "비밀번호는 최소 8자에서 최대 255자까지 가능합니다.")
  private String password;

  @NotBlank(message = "비밀번호 확인은 필수 항목입니다.")
  @Size(min = 8, max = 255, message = "비밀번호 확인은 최소 8자에서 최대 255자까지 가능합니다.")
  private String confirmPassword;

  @NotBlank(message = "이메일은 필수 항목입니다.")
  @Email(message = "유효한 이메일 주소를 입력하세요.")
  @Size(max = 255, message = "이메일은 최대 255자까지 가능합니다.")
  private String email;

  @NotBlank(message = "전화번호는 필수 항목입니다.")
  @Size(max = 15, message = "전화번호는 최대 15자까지 가능합니다.")
  private String phoneNumber;  // 전화번호

  @Size(max = 13, message = "주민등록번호는 최대 13자까지 가능합니다.")
  private String residentRegistrationNumber; // 주민등록번호

  @Size(max = 13, message = "외국인등록번호는 최대 13자까지 가능합니다.")
  private String foreignerRegistrationNumber; // 외국인등록번호
}