package com.sparta.fitpleprojectbackend.user.service;

import com.sparta.fitpleprojectbackend.enums.ErrorType;
import com.sparta.fitpleprojectbackend.enums.Role;
import com.sparta.fitpleprojectbackend.exception.CustomException;
import com.sparta.fitpleprojectbackend.security.UserDetailsImpl;
import com.sparta.fitpleprojectbackend.user.dto.UpdatePasswordRequest;
import com.sparta.fitpleprojectbackend.user.dto.UpdateUserProfileRequest;
import com.sparta.fitpleprojectbackend.user.dto.UserSignupRequest;
import com.sparta.fitpleprojectbackend.user.dto.ReadUserResponse;
import com.sparta.fitpleprojectbackend.user.entity.User;
import com.sparta.fitpleprojectbackend.user.exception.UserException;
import com.sparta.fitpleprojectbackend.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;


  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }


  /**
   * 사용자 회원가입
   *
   * @param request 회원가입에 필요한 정보가 담긴 요청 객체
   * @return 등록된 사용자 객체
   * @throws CustomException 중복된 사용자 정보가 있을 경우 발생
   */
  public User signup(UserSignupRequest request) {
    Optional<User> existingUserByUsername = userRepository.findByAccountIdAndStatus(
        request.getAccountId(), "ACTIVE");
    Optional<User> existingUserByEmail = userRepository.findByEmailAndStatus(request.getEmail(),
        "ACTIVE");
    Optional<User> existingUserByPhoneNumber = userRepository.findByPhoneNumberAndStatus(
        request.getPhoneNumber(), "ACTIVE");

    if (existingUserByUsername.isPresent()) {
      throw new CustomException(ErrorType.DUPLICATE_USERNAME);
    }
    if (existingUserByEmail.isPresent()) {
      throw new CustomException(ErrorType.DUPLICATE_EMAIL);
    }
    if (existingUserByPhoneNumber.isPresent()) {
      throw new CustomException(ErrorType.DUPLICATE_USER);
    }

    Optional<User> deletedUserByUsername = userRepository.findByAccountIdAndStatus(
        request.getAccountId(), "DELETED");
    if (deletedUserByUsername.isPresent()) {
      User user = deletedUserByUsername.get();
      User updatedUser = new User(
          user.getUserName(),
          user.getBalance(),
          user.getResidentRegistrationNumber(),
          user.getForeignerRegistrationNumber(),
          user.getIsForeigner(),
          user.getAccountId(),
          passwordEncoder.encode(request.getPassword()),
          user.getNickname(),
          user.getEmail(),
          user.getUserPicture(),
          "ACTIVE",
          user.getZipcode(),
          user.getMainAddress(),
          user.getDetailedAddress(),
          user.getPhoneNumber(),
          user.getRole(),
          user.getDeletedAt(),
          LocalDateTime.now()
      );
      return userRepository.save(updatedUser);
    }

    User newUser = new User(
        request.getUserName(),
        request.getBalance(),
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
        null
    );

    return userRepository.save(newUser);
  }

  /**
   * 사용자 탈퇴
   *
   * @param username 탈퇴 사용자 계정 ID
   * @throws UserException 사용자를 찾을 수 없는 경우 발생
   */
  public void deleteUser(String username) {
    Optional<User> userOptional = userRepository.findByAccountIdAndStatus(username, "ACTIVE");
    User user = userOptional.orElseThrow(() -> new UserException(ErrorType.NOT_FOUND_USER));

    User updatedUser = new User(
        user.getUserName(),
        user.getBalance(),
        user.getResidentRegistrationNumber(),
        user.getForeignerRegistrationNumber(),
        user.getIsForeigner(),
        user.getAccountId(),
        user.getPassword(),
        user.getNickname(),
        user.getEmail(),
        user.getUserPicture(),
        "DELETED",
        user.getZipcode(),
        user.getMainAddress(),
        user.getDetailedAddress(),
        user.getPhoneNumber(),
        user.getRole(),
        LocalDateTime.now(),
        LocalDateTime.now().plusDays(30)
    );

    userRepository.save(updatedUser);
  }

  /**
   * 유저 프로필 변경
   *
   * @param userDetails 유저 정보
   * @param userRequest 새 프로필 정보
   * @throws UserException 유저를 찾을 수 없는 경우 발생
   */
  @Transactional
  public void updateUserProfile(UpdateUserProfileRequest userRequest, UserDetailsImpl userDetails) {
    Optional<User> userOptional = userRepository.findByAccountIdAndStatus(userDetails.getUsername(),
        "ACTIVE");
    User user = userOptional.orElseThrow(() -> new UserException(ErrorType.NOT_FOUND_USER));

    if (!passwordEncoder.matches(userRequest.getPassword(), user.getPassword())) {
      throw new UserException(ErrorType.INVALID_PASSWORD);
    }

    user.updateUserProfile(userRequest);
  }

  /**
   * 유저 비밀번호 변경
   *
   * @param userDetails 유저 정보
   * @param userRequest 구, 새 비밀번호 정보
   * @throws UserException 유저를 찾을 수 없는 경우 발생
   */
  @Transactional
  public void updateUserPassword(UpdatePasswordRequest userRequest, UserDetailsImpl userDetails) {
    Optional<User> userOptional = userRepository.findByAccountIdAndStatus(userDetails.getUsername(),
        "ACTIVE");
    User user = userOptional.orElseThrow(() -> new UserException(ErrorType.NOT_FOUND_USER));

    if (!passwordEncoder.matches(userRequest.getOldPassword(), user.getPassword())) {
      throw new UserException(ErrorType.INVALID_PASSWORD);
    }

    user.updatePassword(passwordEncoder.encode(userRequest.getNewPassword()));
  }

  /**
   * 유저 프로필 조회
   *
   * @param userDetails 유저 정보
   */
  public ReadUserResponse readUserProfile(UserDetailsImpl userDetails) {
    Optional<User> userOptional = userRepository.findByAccountIdAndStatus(userDetails.getUsername(),
        "ACTIVE");
    User user = userOptional.orElseThrow(() -> new UserException(ErrorType.NOT_FOUND_USER));
    return new ReadUserResponse(user);
  }
}