package com.sparta.fltpleprojectbackend.owner.service;

import com.sparta.fltpleprojectbackend.enums.ErrorType;
import com.sparta.fltpleprojectbackend.enums.Role;
import com.sparta.fltpleprojectbackend.exception.CustomException;
import com.sparta.fltpleprojectbackend.owner.dto.OwnerSignupRequest;
import com.sparta.fltpleprojectbackend.owner.entity.Owner;
import com.sparta.fltpleprojectbackend.owner.repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OwnerService {

  private final OwnerRepository ownerRepository;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public OwnerService(OwnerRepository ownerRepository, PasswordEncoder passwordEncoder) {
    this.ownerRepository = ownerRepository;
    this.passwordEncoder = passwordEncoder;
  }

  // 점주 회원가입 로직
  public Owner signup(OwnerSignupRequest request) {
    if ((request.getResidentRegistrationNumber() == null || request.getResidentRegistrationNumber().isEmpty()) &&
        (request.getForeignerRegistrationNumber() == null || request.getForeignerRegistrationNumber().isEmpty())) {
      throw new CustomException(ErrorType.INVALID_INPUT, "주민등록번호 또는 외국인등록번호 중 하나는 필수 항목입니다.");
    }

    Optional<Owner> existingOwnerByUsername = ownerRepository.findByAccountIdAndOwnerStatus(
        request.getAccountId(), "ACTIVE");
    Optional<Owner> existingOwnerByEmail = ownerRepository.findByEmailAndOwnerStatus(request.getEmail(),
        "ACTIVE");
    Optional<Owner> existingOwnerByPhoneNumber = ownerRepository.findByOwnerPhoneNumberAndOwnerStatus(
        request.getOwnerPhoneNumber(), "ACTIVE");

    if (existingOwnerByUsername.isPresent()) {
      throw new CustomException(ErrorType.DUPLICATE_USERNAME, "주민등록번호 또는 외국인등록번호 중 하나는 필수 항목입니다.");
    }
    if (existingOwnerByEmail.isPresent()) {
      throw new CustomException(ErrorType.DUPLICATE_EMAIL, "주민등록번호 또는 외국인등록번호 중 하나는 필수 항목입니다.");
    }
    if (existingOwnerByPhoneNumber.isPresent()) {
      throw new CustomException(ErrorType.DUPLICATE_USER, "주민등록번호 또는 외국인등록번호 중 하나는 필수 항목입니다.");
    }

    Optional<Owner> deletedOwnerByUsername = ownerRepository.findByAccountIdAndOwnerStatus(
        request.getAccountId(), "DELETED");
    if (deletedOwnerByUsername.isPresent()) {
      Owner owner = deletedOwnerByUsername.get();
      owner.setOwnerStatus("ACTIVE");
      owner.setDeletedAt(null);
      owner.setScheduledDeletionDate(null);
      owner.setPassword(passwordEncoder.encode(request.getPassword()));
      owner.setUpdatedAt(LocalDateTime.now());
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
        request.getZipcode(),
        request.getMainAddress(),
        request.getDetailedAddress(),
        request.getOwnerPhoneNumber(),
        Role.OWNER,
        LocalDateTime.now(),
        null,
        null,
        null
    );

    return ownerRepository.save(newOwner);
  }

  // 점주 회원탈퇴 로직
  public void deleteOwner(String username) {
    Optional<Owner> ownerOptional = ownerRepository.findByAccountIdAndOwnerStatus(username, "ACTIVE");
    Owner owner = ownerOptional.orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_USER,
        "주민등록번호 또는 외국인등록번호 중 하나는 필수 항목입니다."));

    owner.setOwnerStatus("DELETED");
    owner.setDeletedAt(LocalDateTime.now());
    owner.setScheduledDeletionDate(LocalDateTime.now().plusDays(30));
    ownerRepository.save(owner);
  }
}