package com.sparta.fitpleprojectbackend.owner.entity;

import com.sparta.fitpleprojectbackend.common.TimeStamped;
import com.sparta.fitpleprojectbackend.enums.Role;
import com.sparta.fitpleprojectbackend.owner.dto.UpdateOwnerProfileRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Entity
public class Owner extends TimeStamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 10)
  private String ownerName; // 점주 이름

  @Column(length = 13)
  private String residentRegistrationNumber; // 주민등록번호

  @Column(length = 13)
  private String foreignerRegistrationNumber; // 외국인등록번호

  @Column(nullable = false)
  private Boolean isForeigner = false; // 국적구분

  @Column(nullable = false, length = 30, unique = true)
  private String accountId; // 아이디

  @Column(nullable = false, length = 255)
  private String password; // 비밀번호

  @Column(length = 10)
  private String nickname = ""; // 닉네임

  @Column(nullable = false, length = 255)
  private String email; // 이메일

  @Column(length = 255)
  private String ownerPicture = ""; // 점주 이미지

  @Column(nullable = false, length = 10)
  private String ownerStatus = "ACTIVE"; // 점주 상태

  @Column(length = 10)
  private String businessRegistrationNumber; // 사업자등록번호

  @Column(nullable = false, length = 255)
  private String businessName; // 상호명 (법인명)

  @Column(length = 10)
  private String zipcode = ""; // 우편번호

  @Column(length = 255)
  private String mainAddress = ""; // 메인주소

  @Column(length = 255)
  private String detailedAddress = ""; // 상세주소

  @Column(length = 15)
  private String ownerPhoneNumber = ""; // 전화번호

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role; // 권한

  @Column
  private LocalDateTime deletedAt; // 삭제일

  @Column
  private LocalDateTime scheduledDeletionDate; // 삭제 예정일


  public Owner() {

  }

  public Owner(String ownerName, String residentRegistrationNumber,
      String foreignerRegistrationNumber, Boolean isForeigner,
      String accountId, String password, String nickname, String email, String ownerPicture,
      String ownerStatus,
      String businessRegistrationNumber, String businessName, String zipcode, String mainAddress,
      String detailedAddress, String ownerPhoneNumber, Role role, LocalDateTime deletedAt,
      LocalDateTime scheduledDeletionDate) {
    this.ownerName = ownerName;
    this.residentRegistrationNumber = residentRegistrationNumber;
    this.foreignerRegistrationNumber = foreignerRegistrationNumber;
    this.isForeigner = isForeigner != null ? isForeigner : false;
    this.accountId = accountId;
    this.password = password;
    this.nickname = nickname != null ? nickname : "";
    this.email = email;
    this.ownerPicture = ownerPicture != null ? ownerPicture : "";
    this.ownerStatus = ownerStatus != null ? ownerStatus : "ACTIVE";
    this.businessRegistrationNumber = businessRegistrationNumber;
    this.businessName = businessName;
    this.zipcode = zipcode != null ? zipcode : "";
    this.mainAddress = mainAddress != null ? mainAddress : "";
    this.detailedAddress = detailedAddress != null ? detailedAddress : "";
    this.ownerPhoneNumber = ownerPhoneNumber != null ? ownerPhoneNumber : "";
    this.role = role;
    this.deletedAt = deletedAt;
    this.scheduledDeletionDate = scheduledDeletionDate;
  }

  @Override
  public String toString() {
    return "Owner{" +
        "id=" + id +
        ", ownerName='" + ownerName + '\'' +
        ", accountId='" + accountId + '\'' +
        ", email='" + email + '\'' +
        ", ownerPhoneNumber='" + ownerPhoneNumber + '\'' +
        ", role=" + role +
        ", deletedAt=" + deletedAt +
        ", scheduledDeletionDate=" + scheduledDeletionDate +
        '}';
  }

  /**
   * 프로필 변경
   *
   * @param ownerRequest 새 프로필 정보
   */
  public void updateOwnerProfile(UpdateOwnerProfileRequest ownerRequest) {
    this.nickname = ownerRequest.getNickname();
    this.email = ownerRequest.getEmail();
    this.ownerPicture = ownerRequest.getOwnerPicture();
    this.zipcode = ownerRequest.getZipcode();
    this.mainAddress = ownerRequest.getMainAddress();
    this.detailedAddress = ownerRequest.getDetailedAddress();
    this.ownerPhoneNumber = ownerRequest.getOwnerPhoneNumber();
  }

  /**
   * 비밀번호 변경
   *
   * @param newPassword 새 비밀번호 정보
   */
  public void updatePassword(String newPassword) {
    this.password = newPassword;
  }
}