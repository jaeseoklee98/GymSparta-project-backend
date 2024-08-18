package com.sparta.gymspartaprojectbackend.owner.service;

import com.sparta.gymspartaprojectbackend.enums.ErrorType;
import com.sparta.gymspartaprojectbackend.enums.Role;
import com.sparta.gymspartaprojectbackend.exception.CustomException;
import com.sparta.gymspartaprojectbackend.owner.dto.OwnerSignupRequest;
import com.sparta.gymspartaprojectbackend.owner.dto.ReadOwnerResponse;
import com.sparta.gymspartaprojectbackend.owner.dto.UpdateOwnerPasswordRequest;
import com.sparta.gymspartaprojectbackend.owner.dto.UpdateOwnerProfileRequest;
import com.sparta.gymspartaprojectbackend.owner.entity.Owner;
import com.sparta.gymspartaprojectbackend.owner.exception.OwnerException;
import com.sparta.gymspartaprojectbackend.owner.repository.OwnerRepository;
import com.sparta.gymspartaprojectbackend.security.UserDetailsImpl;
import com.sparta.gymspartaprojectbackend.trainer.dto.TrainerGetResponse;
import com.sparta.gymspartaprojectbackend.trainer.exception.TrainerException;
import com.sparta.gymspartaprojectbackend.trainer.repository.TrainerRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OwnerService {

  private final OwnerRepository ownerRepository;
  private final PasswordEncoder passwordEncoder;
  private final TrainerRepository trainerRepository;

  public OwnerService(OwnerRepository ownerRepository, PasswordEncoder passwordEncoder,
      TrainerRepository trainerRepository) {
    this.ownerRepository = ownerRepository;
    this.passwordEncoder = passwordEncoder;
    this.trainerRepository = trainerRepository;
  }

  /**
   * 점주 회원가입
   *
   * @param request 점주 회원가입 요청 정보
   * @return 저장된 점주 정보
   */
  public Owner signup(OwnerSignupRequest request) {
    if ((request.getResidentRegistrationNumber() == null || request.getResidentRegistrationNumber().isEmpty()) &&
        (request.getForeignerRegistrationNumber() == null || request.getForeignerRegistrationNumber().isEmpty())) {
      throw new CustomException(ErrorType.INVALID_INPUT);
    }

    Optional<Owner> existingOwnerByUsername = ownerRepository.findByAccountIdAndOwnerStatus(
        request.getAccountId(), "ACTIVE");
    Optional<Owner> existingOwnerByEmail = ownerRepository.findByEmailAndOwnerStatus(
        request.getEmail(),
        "ACTIVE");
    Optional<Owner> existingOwnerByPhoneNumber = ownerRepository.findByOwnerPhoneNumberAndOwnerStatus(
        request.getOwnerPhoneNumber(), "ACTIVE");

    if (existingOwnerByUsername.isPresent()) {
      throw new CustomException(ErrorType.DUPLICATE_USERNAME);
    }
    if (existingOwnerByEmail.isPresent()) {
      throw new CustomException(ErrorType.DUPLICATE_EMAIL);
    }
    if (existingOwnerByPhoneNumber.isPresent()) {
      throw new CustomException(ErrorType.DUPLICATE_USER);
    }

    Optional<Owner> deletedOwnerByUsername = ownerRepository.findByAccountIdAndOwnerStatus(
        request.getAccountId(), "DELETED");
    if (deletedOwnerByUsername.isPresent()) {
      Owner owner = deletedOwnerByUsername.get();
      Owner updatedOwner = new Owner(
          owner.getOwnerName(),
          owner.getResidentRegistrationNumber(),
          owner.getForeignerRegistrationNumber(),
          owner.getIsForeigner(),
          owner.getAccountId(),
          passwordEncoder.encode(request.getPassword()),
          owner.getNickname(),
          owner.getEmail(),
          owner.getOwnerPicture(),
          "ACTIVE",
          owner.getBusinessRegistrationNumber(),
          owner.getBusinessName(),
          owner.getZipcode(),
          owner.getMainAddress(),
          owner.getDetailedAddress(),
          owner.getOwnerPhoneNumber(),
          owner.getRole(),
          owner.getDeletedAt(),
          LocalDateTime.now()
      );
      return ownerRepository.save(owner);
    }

    Owner newOwner = new Owner(
        request.getOwnerName(),
        request.getResidentRegistrationNumber(),
        request.getForeignerRegistrationNumber(),
        request.getIsForeigner(),
        request.getAccountId(),
        passwordEncoder.encode(request.getPassword()),
        request.getNickname(),
        request.getEmail(),
        null,  // ownerPicture는 null로 설정
        "ACTIVE",
        request.getBusinessRegistrationNumber(),
        request.getBusinessName(),
        request.getZipcode(),
        request.getMainAddress(),
        request.getDetailedAddress(),
        request.getOwnerPhoneNumber(),
        Role.OWNER,
        LocalDateTime.now(),
        null
    );

    return ownerRepository.save(newOwner);
  }

  /**
   * 점주 회원탈퇴
   *
   * @param username 점주 계정 아이디
   */
  public void deleteOwner(String username) {
    Optional<Owner> ownerOptional = ownerRepository.findByAccountIdAndOwnerStatus(username,
        "ACTIVE");
    Owner owner = ownerOptional.orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_USER
    ));

    Owner updatedOwner = new Owner(
        owner.getOwnerName(),
        owner.getResidentRegistrationNumber(),
        owner.getForeignerRegistrationNumber(),
        owner.getIsForeigner(),
        owner.getAccountId(),
        owner.getPassword(),
        owner.getNickname(),
        owner.getEmail(),
        owner.getOwnerPicture(),
        "DELETED",
        owner.getBusinessRegistrationNumber(),
        owner.getBusinessName(),
        owner.getZipcode(),
        owner.getMainAddress(),
        owner.getDetailedAddress(),
        owner.getOwnerPhoneNumber(),
        owner.getRole(),
        LocalDateTime.now(),
        LocalDateTime.now().plusDays(30)
    );
    ownerRepository.save(owner);
  }

  /**
   * . 점주 프로필 조회
   *
   * @param userDetails 점주 정보
   */
  public ReadOwnerResponse readOwnerProfile(UserDetailsImpl userDetails) {
    Optional<Owner> ownerOptional = ownerRepository.findByAccountIdAndOwnerStatus(userDetails.getUsername(), "ACTIVE");
    Owner owner = ownerOptional.orElseThrow(() -> new OwnerException(ErrorType.NOT_FOUND_OWNER));
    return new ReadOwnerResponse(owner);
  }

  /**
   * 점주 프로필 변경
   *
   * @param userDetails 점주 정보
   * @param ownerRequest 새 프로필 정보
   * @throws OwnerException 점주를 찾을 수 없는 경우 발생, 비밀번호 확인이 맞지 않으면 발생
   */
  @Transactional
  public void updateOwnerProfile(UpdateOwnerProfileRequest ownerRequest, UserDetailsImpl userDetails) {
    Optional<Owner> ownerOptional = ownerRepository.findByAccountIdAndOwnerStatus(userDetails.getUsername(), "ACTIVE");
    Owner owner = ownerOptional.orElseThrow(() -> new OwnerException(ErrorType.NOT_FOUND_OWNER));

    if (!passwordEncoder.matches(ownerRequest.getPassword(), owner.getPassword())) {
      throw new OwnerException(ErrorType.INVALID_PASSWORD);
    }

    owner.updateOwnerProfile(ownerRequest);
  }
  /**.
   * 점주 비밀번호 변경
   *
   * @param userDetails 점주 정보
   * @param ownerRequest 새 비밀번호 정보
   * @throws OwnerException 점주를 찾을 수 없는 경우 발생, 비밀번호 확인이 맞지 않으면 발생
   */
  @Transactional
  public void updateOwnerPassword(UpdateOwnerPasswordRequest ownerRequest, UserDetailsImpl userDetails) {

    Optional<Owner> ownerOptional = ownerRepository.findByAccountIdAndOwnerStatus(
        userDetails.getUsername(), "ACTIVE");
    Owner owner = ownerOptional.orElseThrow(() -> new OwnerException(ErrorType.NOT_FOUND_OWNER));

    if (!passwordEncoder.matches(ownerRequest.getOldPassword(), owner.getPassword())) {
      throw new OwnerException(ErrorType.INVALID_PASSWORD);
    }

    owner.updatePassword(passwordEncoder.encode(ownerRequest.getNewPassword()));
  }

  /**.
   * 점주의 모든 트레이너 조회
   *
   * @param userDetails 오너 정보
   * @throws TrainerException 트레이너를 찾을 수 없는 경우 발생
   */
  public List<TrainerGetResponse> getAllMyTrainers(UserDetailsImpl userDetails) {

    Long ownerId = userDetails.getOwner().getId();

    return trainerRepository.findAllTrainersByOwnerId(ownerId).stream()
        .map(TrainerGetResponse::new)
        .toList();
  }
}