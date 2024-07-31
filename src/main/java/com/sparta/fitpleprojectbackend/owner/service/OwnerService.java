package com.sparta.fitpleprojectbackend.owner.service;

import com.sparta.fitpleprojectbackend.enums.ErrorType;
import com.sparta.fitpleprojectbackend.enums.Role;
import com.sparta.fitpleprojectbackend.exception.CustomException;
import com.sparta.fitpleprojectbackend.owner.dto.OwnerSignupRequest;
import com.sparta.fitpleprojectbackend.owner.dto.ReadOwnerResponse;
import com.sparta.fitpleprojectbackend.owner.dto.UpdateOwnerPasswordRequest;
import com.sparta.fitpleprojectbackend.owner.dto.UpdateOwnerProfileRequest;
import com.sparta.fitpleprojectbackend.owner.entity.Owner;
import com.sparta.fitpleprojectbackend.owner.exception.OwnerException;
import com.sparta.fitpleprojectbackend.owner.repository.OwnerRepository;
import com.sparta.fitpleprojectbackend.security.UserDetailsImpl;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OwnerService {

  private final OwnerRepository ownerRepository;
  private final PasswordEncoder passwordEncoder;

  public OwnerService(OwnerRepository ownerRepository, PasswordEncoder passwordEncoder) {
    this.ownerRepository = ownerRepository;
    this.passwordEncoder = passwordEncoder;
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
  /**
   * 유저 비밀번호 변경
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
}