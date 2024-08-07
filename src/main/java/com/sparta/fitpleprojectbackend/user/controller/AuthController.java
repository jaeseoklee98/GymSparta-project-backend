package com.sparta.fitpleprojectbackend.user.controller;

import com.sparta.fitpleprojectbackend.common.CommonResponse;
import com.sparta.fitpleprojectbackend.enums.Role;
import com.sparta.fitpleprojectbackend.jwtutil.JwtUtil;
import com.sparta.fitpleprojectbackend.security.UserDetailsImpl;
import com.sparta.fitpleprojectbackend.user.dto.LoginRequest;
import com.sparta.fitpleprojectbackend.user.repository.UserRepository;
import com.sparta.fitpleprojectbackend.owner.repository.OwnerRepository;
import com.sparta.fitpleprojectbackend.trainer.repository.TrainerRepository;
import com.sparta.fitpleprojectbackend.user.service.UserService;
import com.sparta.fitpleprojectbackend.owner.service.OwnerService;
import com.sparta.fitpleprojectbackend.trainer.entity.Trainer;
import com.sparta.fitpleprojectbackend.owner.entity.Owner;
import com.sparta.fitpleprojectbackend.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {

  private AuthenticationManager authenticationManager;

  private JwtUtil jwtUtil;

  private UserRepository userRepository;

  private OwnerRepository ownerRepository;

  private TrainerRepository trainerRepository;

  private UserService userService;

  private OwnerService ownerService;

  public AuthController(AuthenticationManager authenticationManager,
      JwtUtil jwtUtil,
      UserRepository userRepository,
      OwnerRepository ownerRepository,
      TrainerRepository trainerRepository,
      UserService userService,
      OwnerService ownerService) {
    this.authenticationManager = authenticationManager;
    this.jwtUtil = jwtUtil;
    this.userRepository = userRepository;
    this.ownerRepository = ownerRepository;
    this.trainerRepository = trainerRepository;
    this.userService = userService;
    this.ownerService = ownerService;
  }

  /**
   * 로그인 처리
   *
   * @param loginRequest 로그인 요청 정보 (아이디, 비밀번호)
   * @return ResponseEntity<ResponseMessage> JWT 액세스 토큰과 리프레시 토큰
   */
  @PostMapping("/login")
  public ResponseEntity<CommonResponse<Map<String, String>>> login(
      @RequestBody LoginRequest loginRequest, HttpServletRequest request) {
    Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
    if (currentAuth != null && currentAuth.isAuthenticated() &&
        !currentAuth.getName().equals("anonymousUser")) {
      CommonResponse<Map<String, String>> response = new CommonResponse<>(
          HttpStatus.CONFLICT.value(), "이미 로그인된 상태입니다.", null);
      return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    try {
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(loginRequest.getAccountId(),
              loginRequest.getPassword())
      );

      UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

      if (userDetails.getRole() == Role.USER) {
        Optional<User> userOptional = userRepository.findByAccountIdAndStatus(
            userDetails.getUsername(), "ACTIVE");
        if (!userOptional.isPresent()) {
          CommonResponse<Map<String, String>> response = new CommonResponse<>(
              HttpStatus.UNAUTHORIZED.value(), "회원탈퇴된 사용자입니다.", null);
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
      } else if (userDetails.getRole() == Role.OWNER) {
        Optional<Owner> ownerOptional = ownerRepository.findByAccountIdAndOwnerStatus(
            userDetails.getUsername(), "ACTIVE");
        if (!ownerOptional.isPresent()) {
          CommonResponse<Map<String, String>> response = new CommonResponse<>(
              HttpStatus.UNAUTHORIZED.value(), "회원탈퇴된 점주입니다.", null);
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
      } else if (userDetails.getRole() == Role.TRAINER) {
        Optional<Trainer> trainerOptional = trainerRepository.findByAccountIdAndTrainerStatus(
            userDetails.getUsername(), "ACTIVE");
        if (!trainerOptional.isPresent()) {
          CommonResponse<Map<String, String>> response = new CommonResponse<>(
              HttpStatus.UNAUTHORIZED.value(), "회원탈퇴된 트레이너입니다.", null);
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
      }

      String accessToken = jwtUtil.generateAccessToken(userDetails.getUsername());
      String refreshToken = jwtUtil.generateRefreshToken(userDetails.getUsername());

      Map<String, String> tokenResponse = new HashMap<>();
      tokenResponse.put("accessToken", accessToken);
      tokenResponse.put("refreshToken", refreshToken);

      CommonResponse<Map<String, String>> response = new CommonResponse<>(
          HttpStatus.OK.value(), "로그인 성공", tokenResponse);
      return ResponseEntity.ok(response);

    } catch (Exception e) {
      CommonResponse<Map<String, String>> response = new CommonResponse<>(
          HttpStatus.UNAUTHORIZED.value(), "로그인 실패", null);
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
  }

  /**
   * 로그아웃 처리
   *
   * @param request HTTP 요청
   * @return ResponseEntity<ResponseMessage < String>> 로그아웃 성공 메시지
   */
  @PostMapping("/logout")
  public ResponseEntity<CommonResponse<String>> logout(HttpServletRequest request) {
    String authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      CommonResponse<String> response = new CommonResponse<>(
          HttpStatus.UNAUTHORIZED.value(), "로그인 먼저 해주세요.", null);
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    String token = authHeader.substring(7);
    if (!jwtUtil.validateToken(token)) {
      CommonResponse<String> response = new CommonResponse<>(
          HttpStatus.UNAUTHORIZED.value(), "유효하지 않은 토큰입니다.", null);
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    String username = jwtUtil.getUsername(token);
    if (username == null) {
      CommonResponse<String> response = new CommonResponse<>(
          HttpStatus.UNAUTHORIZED.value(), "로그인되지 않은 상태입니다.", null);
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    Optional<User> userOptional = userRepository.findByAccountIdAndStatus(username, "ACTIVE");
    Optional<Owner> ownerOptional = ownerRepository.findByAccountIdAndOwnerStatus(username, "ACTIVE");
    Optional<Trainer> trainerOptional = trainerRepository.findByAccountIdAndTrainerStatus(username, "ACTIVE");

    if (userOptional.isEmpty() && ownerOptional.isEmpty() && trainerOptional.isEmpty()) {
      CommonResponse<String> response = new CommonResponse<>(
          HttpStatus.UNAUTHORIZED.value(), "이미 로그아웃된 상태입니다.", null);
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    SecurityContextHolder.clearContext();
    CommonResponse<String> response = new CommonResponse<>(
        HttpStatus.OK.value(), "로그아웃 성공", "로그아웃이 완료되었습니다.");
    return ResponseEntity.ok(response);
  }
}