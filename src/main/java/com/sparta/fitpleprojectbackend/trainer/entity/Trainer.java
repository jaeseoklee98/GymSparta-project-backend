package com.sparta.fitpleprojectbackend.trainer.entity;

import com.sparta.fitpleprojectbackend.common.TimeStamped;
import com.sparta.fitpleprojectbackend.enums.Role;
import com.sparta.fitpleprojectbackend.store.entity.Store;
import com.sparta.fitpleprojectbackend.trainer.dto.UpdateTrainerProfileRequest;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
@Entity
public class Trainer extends TimeStamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private Double ptPrice; // pt 가격

  @Column
  private boolean isMembership; // 회원권 유무

  @Column(length = 10)
  private String trainerName; // 트레이너 이름

  @Column(length = 255)
  private String trainerInfo; // 트레이너 소개

  @Column(nullable = false, length = 10, unique = true)
  private String accountId; // 아이디

  @Column(nullable = false, length = 255)
  private String password; // 비밀번호

  @Column(nullable = false, length = 10)
  private String nickname; // 닉네임

  @Column(nullable = false, length = 255)
  private String email; // 이메일

  @Column(length = 255)
  private String trainerPicture; // 트레이너 이미지

  @Column(nullable = false, length = 10)
  private String trainerStatus; // 트레이너 상태

  @Column(nullable = false, length = 15)
  private String trainerPhoneNumber; // 전화번호

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role; // 권한

  @Column
  private LocalDateTime deletedAt; // 삭제일

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "store_id", nullable = false)
  private Store store;


  public Trainer() {
  }

  public Trainer(String trainerName, Double ptPrice, String trainerInfo, String accountId,
      String password, String nickname, String email, String trainerPicture, String trainerStatus,
      String trainerPhoneNumber, Role role, LocalDateTime deletedAt) {
    this.trainerName = trainerName;
    this.ptPrice = ptPrice;
    this.trainerInfo = trainerInfo;
    this.accountId = accountId;
    this.password = password;
    this.nickname = nickname;
    this.email = email;
    this.trainerPicture = trainerPicture;
    this.trainerStatus = trainerStatus;
    this.trainerPhoneNumber = trainerPhoneNumber;
    this.role = role;
    this.deletedAt = deletedAt;
  }

  @Override
  public String toString() {
    return "Trainer{" +
        "id=" + id +
        ", trainerName='" + trainerName + '\'' +
        ", trainerInfo='" + trainerInfo + '\'' +
        ", accountId='" + accountId + '\'' +
        ", password='" + password + '\'' +
        ", nickname='" + nickname + '\'' +
        ", email='" + email + '\'' +
        ", trainerPicture='" + trainerPicture + '\'' +
        ", trainerStatus='" + trainerStatus + '\'' +
        ", trainerPhoneNumber='" + trainerPhoneNumber + '\'' +
        ", role=" + role +
        ", deletedAt=" + deletedAt +
        '}';
  }

  public Trainer(String trainerName, double ptPrice) {
    this.trainerName = trainerName;
    this.ptPrice = ptPrice;
  }

  /**.
   * 프로필 변경
   *
   * @param trainerRequest 새 프로필 정보
   */
  public void updateUserProfile (UpdateTrainerProfileRequest trainerRequest) {
    this.trainerName = trainerRequest.getTrainerName();
    this.nickname = trainerRequest.getNickname();
    this.trainerInfo = trainerRequest.getTrainerInfo();
    this.email = trainerRequest.getEmail();
    this.trainerPhoneNumber = trainerRequest.getTrainerPhoneNumber();
    this.trainerPicture = trainerRequest.getTrainerPicture();
  }

  /**.
   * 비밀번호 변경
   *
   * @param newPassword 새 비밀번호 정보
   */
  public void updatePassword (String newPassword) {
    this.password = newPassword;
  }
}