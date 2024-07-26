package com.sparta.fltpleprojectbackend.user.controller;

import com.sparta.fltpleprojectbackend.enums.Role;
import com.sparta.fltpleprojectbackend.jwtutil.JwtUtil;
import com.sparta.fltpleprojectbackend.security.UserDetailsImpl;
import com.sparta.fltpleprojectbackend.user.dto.LoginRequest;
import com.sparta.fltpleprojectbackend.user.dto.ResponseMessage;
import com.sparta.fltpleprojectbackend.user.entity.User;
import com.sparta.fltpleprojectbackend.user.repository.UserRepository;
import com.sparta.fltpleprojectbackend.user.service.UserService;
import com.sparta.fltpleprojectbackend.owner.repository.OwnerRepository;
import com.sparta.fltpleprojectbackend.owner.entity.Owner;
import com.sparta.fltpleprojectbackend.owner.service.OwnerService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private OwnerRepository ownerRepository;

  @Autowired
  private UserService userService;

  @Autowired
  private OwnerService ownerService;

  /**
   * 로그인 처리
   * @param loginRequest 로그인 요청 정보 (아이디, 비밀번호)
   * @return ResponseEntity<ResponseMessage> JWT 액세스 토큰과 리프레시 토큰
   */
  @PostMapping("/login")
  public ResponseEntity<ResponseMessage> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
    // 현재 로그인된 상태인지 확인
    Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
    if (currentAuth != null && currentAuth.isAuthenticated() &&
        !currentAuth.getName().equals("anonymousUser")) {
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body(ResponseMessage.error("이미 로그인된 상태입니다."));
    }

    try {
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(loginRequest.getAccountId(), loginRequest.getPassword())
      );

      UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

      // 사용자 또는 점주 상태 확인
      if (userDetails.getRole() == Role.USER) {
        Optional<User> userOptional = userRepository.findByAccountIdAndStatus(userDetails.getUsername(), "ACTIVE");
        if (!userOptional.isPresent()) {
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
              .body(ResponseMessage.error("회원탈퇴된 사용자입니다."));
        }
      } else if (userDetails.getRole() == Role.OWNER) {
        Optional<Owner> ownerOptional = ownerRepository.findByAccountIdAndOwnerStatus(userDetails.getUsername(), "ACTIVE");
        if (!ownerOptional.isPresent()) {
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
              .body(ResponseMessage.error("회원탈퇴된 점주입니다."));
        }
      }

      String accessToken = jwtUtil.generateAccessToken(userDetails.getUsername());
      String refreshToken = jwtUtil.generateRefreshToken(userDetails.getUsername());

      ResponseMessage response = ResponseMessage.success("로그인 성공");
      return ResponseEntity.ok()
          .header("Authorization", "Bearer " + accessToken)
          .header("Refresh-Token", refreshToken)
          .body(response);

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(ResponseMessage.error("로그인 실패: " + e.getMessage()));
    }
  }

  /**
   * 로그아웃 처리
   * @param request HTTP 요청
   * @return ResponseEntity<ResponseMessage<String>> 로그아웃 성공 메시지
   */
  @PostMapping("/logout")
  public ResponseEntity<ResponseMessage> logout(HttpServletRequest request) {
    String authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(ResponseMessage.error("로그인 먼저 해주세요."));
    }

    String token = authHeader.substring(7);
    if (!jwtUtil.validateToken(token)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(ResponseMessage.error("유효하지 않은 토큰입니다."));
    }

    String username = jwtUtil.getUsername(token);
    if (username == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(ResponseMessage.error("로그인되지 않은 상태입니다."));
    }

    Optional<User> userOptional = userRepository.findByAccountIdAndStatus(username, "ACTIVE");
    Optional<Owner> ownerOptional = ownerRepository.findByAccountIdAndOwnerStatus(username, "ACTIVE");

    if (!userOptional.isPresent() && !ownerOptional.isPresent()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(ResponseMessage.error("이미 로그아웃된 상태입니다."));
    }

    // 인증된 상태에서 로그아웃 처리
    SecurityContextHolder.clearContext();
    ResponseMessage response = ResponseMessage.success("로그아웃 성공");
    return ResponseEntity.ok(response);
  }
}