package com.sparta.fltpleprojectbackend.user.service;

import com.sparta.fltpleprojectbackend.enums.ErrorType;
import com.sparta.fltpleprojectbackend.enums.Role;
import com.sparta.fltpleprojectbackend.exception.CustomException;
import com.sparta.fltpleprojectbackend.security.UserDetailsImpl;
import com.sparta.fltpleprojectbackend.user.dto.UpdatePasswordRequest;
import com.sparta.fltpleprojectbackend.user.dto.UpdateUserProfileRequest;
import com.sparta.fltpleprojectbackend.user.dto.UserSignupRequest;
import com.sparta.fltpleprojectbackend.user.dto.ReadUserResponse;
import com.sparta.fltpleprojectbackend.user.entity.User;
import com.sparta.fltpleprojectbackend.user.exception.UserException;
import com.sparta.fltpleprojectbackend.user.repository.UserRepository;
import jakarta.transaction.Transactional;
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
    Optional<User> existingUserByUsername = userRepository.findByAccountIdAndStatus(
        request.getAccountId(), "ACTIVE");
    Optional<User> existingUserByEmail = userRepository.findByEmailAndStatus(request.getEmail(),
        "ACTIVE");
    Optional<User> existingUserByPhoneNumber = userRepository.findByPhoneNumberAndStatus(
        request.getPhoneNumber(), "ACTIVE");

    if (existingUserByUsername.isPresent()) {
      throw new CustomException(ErrorType.DUPLICATE_USERNAME, "주민등록번호 또는 외국인등록번호 중 하나는 필수 항목입니다.");
    }
    if (existingUserByEmail.isPresent()) {
      throw new CustomException(ErrorType.DUPLICATE_EMAIL, "주민등록번호 또는 외국인등록번호 중 하나는 필수 항목입니다.");
    }
    if (existingUserByPhoneNumber.isPresent()) {
      throw new CustomException(ErrorType.DUPLICATE_USER, "주민등록번호 또는 외국인등록번호 중 하나는 필수 항목입니다.");
    }

    Optional<User> deletedUserByUsername = userRepository.findByAccountIdAndStatus(
        request.getAccountId(), "DELETED");
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
        request.getResidentRegistrationNumber(),
        request.getForeignerRegistrationNumber(),
        false,
        request.getAccountId(),
        passwordEncoder.encode(request.getPassword()),
        "",
        request.getEmail(),
        "",
        "ACTIVE",
        "",
        "",
        "",
        request.getPhoneNumber(),
        Role.USER,
        LocalDateTime.now(),
        null,
        null,
        null
    );

    return userRepository.save(newUser);
  }

  public void deleteUser(String username) {
    Optional<User> userOptional = userRepository.findByAccountIdAndStatus(username, "ACTIVE");
    User user = userOptional.orElseThrow(() -> new UserException(ErrorType.NOT_FOUND_USER));

    user.setStatus("DELETED");
    user.setDeletedAt(LocalDateTime.now());
    user.setScheduledDeletionDate(LocalDateTime.now().plusDays(30));
    userRepository.save(user);
  }

  /**.
   * 유저 프로필 변경
   *
   * @param userDetails 유저 정보
   * @param userRequest 새 프로필 정보
   * @throws UserException 유저를 찾을 수 없는 경우 발생
   */
  @Transactional
  public void updateUserProfile(UpdateUserProfileRequest userRequest, UserDetailsImpl userDetails) {
    Optional<User> userOptional = userRepository.findByAccountIdAndStatus(userDetails.getUsername(), "ACTIVE");
    User user = userOptional.orElseThrow(() -> new UserException(ErrorType.NOT_FOUND_USER));

    if (!passwordEncoder.matches(userRequest.getPassword(), user.getPassword())) {
      throw new UserException(ErrorType.INVALID_PASSWORD);
    }

    user.updateUserProfile(userRequest);
  }

  /**.
   * 유저 비밀번호 변경
   *
   * @param userDetails 유저 정보
   * @param userRequest 구, 새 비밀번호 정보
   * @throws UserException 유저를 찾을 수 없는 경우 발생
   */
  @Transactional
  public void updateUserPassword(UpdatePasswordRequest userRequest, UserDetailsImpl userDetails) {
    Optional<User> userOptional = userRepository.findByAccountIdAndStatus(userDetails.getUsername(), "ACTIVE");
    User user = userOptional.orElseThrow(() -> new UserException(ErrorType.NOT_FOUND_USER));

    if (!passwordEncoder.matches(userRequest.getOldPassword(), user.getPassword())) {
      throw new UserException(ErrorType.INVALID_PASSWORD);
    }

    user.updatePassword(passwordEncoder.encode(userRequest.getNewPassword()));
  }

  /**.
   * 유저 프로필 조회
   *
   * @param userDetails 유저 정보
   */
  public ReadUserResponse readUserProfile(UserDetailsImpl userDetails) {
    Optional<User> userOptional = userRepository.findByAccountIdAndStatus(userDetails.getUsername(), "ACTIVE");
    User user = userOptional.orElseThrow(() -> new UserException(ErrorType.NOT_FOUND_USER));
    return new ReadUserResponse(user);
  }
}