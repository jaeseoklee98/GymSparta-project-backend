package com.sparta.fltpleprojectbackend.user.service;

import com.sparta.fltpleprojectbackend.enums.ErrorType;
import com.sparta.fltpleprojectbackend.enums.Role;
import com.sparta.fltpleprojectbackend.user.dto.UserSignupRequest;
import com.sparta.fltpleprojectbackend.user.entity.User;
import com.sparta.fltpleprojectbackend.user.exception.UserException;
import com.sparta.fltpleprojectbackend.user.repository.UserRepository;
// import com.sparta.fltpleprojectbackend.security.RefreshTokenService; // Redis 관련 임시 비활성화
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  // private final RefreshTokenService refreshTokenService; // Redis 관련 임시 비활성화

  @Autowired
  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder /*, RefreshTokenService refreshTokenService */) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    // this.refreshTokenService = refreshTokenService; // Redis 관련 임시 비활성화
  }

  public User signup(UserSignupRequest request) {
    userRepository.findByAccountId(request.getAccountId()).ifPresent(user -> {
      throw new UserException(ErrorType.DUPLICATE_USERNAME);
    });

    userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
      throw new UserException(ErrorType.DUPLICATE_EMAIL);
    });

    User user = new User(
        request.getUserName(),
        null, // 주민등록번호
        null, // 외국인등록번호
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
        "", // 전화번호
        Role.USER,
        LocalDateTime.now(),
        null,
        null,
        null
    );

    return userRepository.save(user);
  }

  public void deleteUser(String username) {
    Optional<User> userOptional = userRepository.findByAccountId(username);
    User user = userOptional.orElseThrow(() -> new UserException(ErrorType.NOT_FOUND_USER));

    user.setDeletedAt(LocalDateTime.now());
    user.setScheduledDeletionDate(LocalDateTime.now().plusDays(30));
    userRepository.save(user);

    // refreshTokenService.deleteByUsername(username); // Redis 관련 임시 비활성화
  }

  public void deleteOwner(String username) {
    Optional<User> userOptional = userRepository.findByAccountId(username);
    User user = userOptional.orElseThrow(() -> new UserException(ErrorType.NOT_FOUND_USER));

    user.setDeletedAt(LocalDateTime.now());
    user.setScheduledDeletionDate(LocalDateTime.now().plusDays(30));
    userRepository.save(user);

    // refreshTokenService.deleteByUsername(username); // Redis 관련 임시 비활성화
  }
}