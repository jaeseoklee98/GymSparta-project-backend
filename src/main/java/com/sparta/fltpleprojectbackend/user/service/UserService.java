package com.sparta.fltpleprojectbackend.user.service;

import com.sparta.fltpleprojectbackend.enums.ErrorType;
import com.sparta.fltpleprojectbackend.enums.Role;
import com.sparta.fltpleprojectbackend.exception.CustomException;
import com.sparta.fltpleprojectbackend.user.dto.UserSignupRequest;
import com.sparta.fltpleprojectbackend.user.entity.User;
import com.sparta.fltpleprojectbackend.user.exception.UserException;
import com.sparta.fltpleprojectbackend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public User signup(UserSignupRequest request) {
    // 중복 확인 (ACTIVE 상태의 사용자만 중복 검사)
    Optional<User> existingUserByUsername = userRepository.findByAccountIdAndStatus(request.getAccountId(), "ACTIVE");
    Optional<User> existingUserByEmail = userRepository.findByEmailAndStatus(request.getEmail(), "ACTIVE");
    Optional<User> existingUserByPhoneNumber = userRepository.findByPhoneNumberAndStatus(request.getPhoneNumber(), "ACTIVE");

    if (existingUserByUsername.isPresent()) {
      throw new CustomException(ErrorType.DUPLICATE_USERNAME);
    }
    if (existingUserByEmail.isPresent()) {
      throw new CustomException(ErrorType.DUPLICATE_EMAIL);
    }
    if (existingUserByPhoneNumber.isPresent()) {
      throw new CustomException(ErrorType.DUPLICATE_USER);
    }

    // DELETED 상태의 사용자가 있는지 확인
    Optional<User> deletedUserByUsername = userRepository.findByAccountIdAndStatus(request.getAccountId(), "DELETED");
    if (deletedUserByUsername.isPresent()) {
      User user = deletedUserByUsername.get();
      user.setStatus("ACTIVE");
      user.setDeletedAt(null);
      user.setScheduledDeletionDate(null);
      user.setPassword(passwordEncoder.encode(request.getPassword()));
      user.setUpdatedAt(LocalDateTime.now());
      return userRepository.save(user);
    }

    User newUser = new User(
        request.getUserName(),
        request.getResidentRegistrationNumber(), // 주민등록번호
        request.getForeignerRegistrationNumber(), // 외국인등록번호
        false, // 국적구분
        request.getAccountId(),
        passwordEncoder.encode(request.getPassword()),
        "", // 닉네임
        request.getEmail(),
        "", // 유저 사진
        "ACTIVE",
        "", // 우편번호
        "", // 메인주소
        "", // 상세주소
        request.getPhoneNumber(),
        Role.USER,
        LocalDateTime.now(),
        null,
        null,
        null
    );

    return userRepository.save(newUser);
  }

  /**
   * 일반 사용자 회원탈퇴 처리
   * @param username 사용자 계정 ID
   */
  public void deleteUser(String username) {
    Optional<User> userOptional = userRepository.findByAccountIdAndStatus(username, "ACTIVE");
    User user = userOptional.orElseThrow(() -> new UserException(ErrorType.NOT_FOUND_USER));

    user.setStatus("DELETED");
    user.setDeletedAt(LocalDateTime.now());
    user.setScheduledDeletionDate(LocalDateTime.now().plusDays(30));
    userRepository.save(user);
  }

  /**
   * 점주 회원탈퇴 처리
   * @param username 점주 계정 ID
   */
  public void deleteOwner(String username) {
    Optional<User> userOptional = userRepository.findByAccountIdAndStatus(username, "ACTIVE");
    User user = userOptional.orElseThrow(() -> new UserException(ErrorType.NOT_FOUND_USER));

    user.setStatus("DELETED");
    user.setDeletedAt(LocalDateTime.now());
    user.setScheduledDeletionDate(LocalDateTime.now().plusDays(30));
    userRepository.save(user);
  }
}