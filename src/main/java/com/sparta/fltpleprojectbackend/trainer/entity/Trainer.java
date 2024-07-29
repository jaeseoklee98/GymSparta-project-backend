package com.sparta.fltpleprojectbackend.trainer.entity;

import com.sparta.fltpleprojectbackend.enums.Role;
import com.sparta.fltpleprojectbackend.store.entity.Store;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Trainer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

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

  @Column(nullable = false)
  private LocalDateTime createdAt; // 생성일

  @Column
  private LocalDateTime updatedAt; // 수정일

  @Column
  private LocalDateTime deletedAt; // 삭제일

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "store_id", nullable = false)
  private Store store;

  // 기본 생성자
  public Trainer() {}

  // 모든 필드를 포함한 생성자
  public Trainer(String trainerName, String trainerInfo, String accountId, String password, String nickname, String email, String trainerPicture, String trainerStatus,
                 String trainerPhoneNumber, Role role, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt, Store store) {
    this.trainerName = trainerName;
    this.trainerInfo = trainerInfo;
    this.accountId = accountId;
    this.password = password;
    this.nickname = nickname;
    this.email = email;
    this.trainerPicture = trainerPicture;
    this.trainerStatus = trainerStatus;
    this.trainerPhoneNumber = trainerPhoneNumber;
    this.role = role;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.deletedAt = deletedAt;
    this.store = store;
  }

  @Override
  public String toString() {
    return "Trainer{" +
        "trainerId=" + id +
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
        ", createdAt=" + createdAt +
        ", updatedAt=" + updatedAt +
        ", deletedAt=" + deletedAt +
        '}';
  }
}