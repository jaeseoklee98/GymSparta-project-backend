package com.sparta.gymspartaprojectbackend.user.service;

import com.sparta.gymspartaprojectbackend.enums.ErrorType;
import com.sparta.gymspartaprojectbackend.enums.Role;
import com.sparta.gymspartaprojectbackend.exception.CustomException;
import com.sparta.gymspartaprojectbackend.security.UserDetailsImpl;
import com.sparta.gymspartaprojectbackend.user.dto.UpdatePasswordRequest;
import com.sparta.gymspartaprojectbackend.user.dto.UpdateUserProfileRequest;
import com.sparta.gymspartaprojectbackend.user.dto.UserSignupRequest;
import com.sparta.gymspartaprojectbackend.user.dto.ReadUserResponse;
import com.sparta.gymspartaprojectbackend.user.entity.User;
import com.sparta.gymspartaprojectbackend.user.exception.UserException;
import com.sparta.gymspartaprojectbackend.user.repository.UserRepository;
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

  public void deleteUser(String username) {
    Optional<User> userOptional = userRepository.findByAccountIdAndStatus(username, "ACTIVE");
    User user = userOptional.orElseThrow(() -> new UserException(ErrorType.NOT_FOUND_USER));

    User updatedUser = new User(
        user.getUserName(),
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

  public ReadUserResponse readUserProfile(UserDetailsImpl userDetails) {
    if (userDetails.getUser() != null) {
      return new ReadUserResponse(userDetails.getUser());
    } else if (userDetails.getOwner() != null) {
      return new ReadUserResponse(userDetails.getOwner());
    } else if (userDetails.getTrainer() != null) {
      return new ReadUserResponse(userDetails.getTrainer());
    } else {
      throw new UserException(ErrorType.NOT_FOUND_USER);
    }
  }
}