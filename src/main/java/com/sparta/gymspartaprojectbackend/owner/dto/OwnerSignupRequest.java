package com.sparta.gymspartaprojectbackend.owner.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class OwnerSignupRequest {

  @NotBlank(message = "점주 이름은 필수 항목입니다.")
  @Size(max = 50, message = "점주 이름은 최대 50자까지 가능합니다.")
  private String ownerName; // 점주 이름

  @Size(max = 13, message = "주민등록번호는 최대 13자까지 가능합니다.")
  private String residentRegistrationNumber; // 주민등록번호

  @Size(max = 13, message = "외국인등록번호는 최대 13자까지 가능합니다.")
  private String foreignerRegistrationNumber; // 외국인등록번호

  @NotBlank(message = "아이디는 필수 항목입니다.")
  @Size(max = 15, message = "아이디는 최대 15자까지 가능합니다.")
  private String accountId; // 아이디

  @NotBlank(message = "비밀번호는 필수 항목입니다.")
  @Size(min = 8, max = 255, message = "비밀번호는 최소 8자에서 최대 255자까지 가능합니다.")
  private String password; // 비밀번호

  @NotBlank(message = "비밀번호 확인은 필수 항목입니다.")
  @Size(min = 8, max = 255, message = "비밀번호 확인은 최소 8자에서 최대 255자까지 가능합니다.")
  private String confirmPassword; // 비밀번호 확인

  @NotBlank(message = "이메일은 필수 항목입니다.")
  @Email(message = "유효한 이메일 주소를 입력하세요.")
  @Size(max = 255, message = "이메일은 최대 255자까지 가능합니다.")
  private String email; // 이메일

  @NotBlank(message = "전화번호는 필수 항목입니다.")
  @Size(max = 15, message = "전화번호는 최대 15자까지 가능합니다.")
  private String ownerPhoneNumber; // 전화번호

  @NotBlank(message = "사업자등록번호는 필수 항목입니다.")
  @Size(max = 10, message = "사업자등록번호는 최대 10자까지 가능합니다.")
  private String businessRegistrationNumber; // 사업자등록번호

  @NotBlank(message = "법인명은 필수 항목입니다.")
  @Size(max = 255, message = "법인명은 최대 255자까지 가능합니다.")
  private String businessName; // 법인명

  @NotBlank(message = "우편번호는 필수 항목입니다.")
  @Size(max = 10, message = "우편번호는 최대 10자까지 가능합니다.")
  private String zipcode; // 우편번호

  @NotBlank(message = "메인 주소는 필수 항목입니다.")
  @Size(max = 255, message = "메인 주소는 최대 255자까지 가능합니다.")
  private String mainAddress; // 메인 주소

  @NotBlank(message = "상세 주소는 필수 항목입니다.")
  @Size(max = 255, message = "상세 주소는 최대 255자까지 가능합니다.")
  private String detailedAddress; // 상세 주소

  @Size(max = 255, message = "닉네임은 최대 255자까지 가능합니다.")
  private String nickname; // 닉네임

  private Boolean isForeigner; // 국적 구분 필드 추가
}