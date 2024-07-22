package com.sparta.fltpleprojectbackend.user.controller;

import com.sparta.fltpleprojectbackend.user.dto.ResponseMessage;
import com.sparta.fltpleprojectbackend.user.dto.UserSignupRequest;
import com.sparta.fltpleprojectbackend.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  /**
   * 유저 회원가입
   * @param request 회원가입 요청 정보 (아이디, 비밀번호, 이메일, 전화번호, 이름)
   * @return ResponseEntity<ResponseMessage<String>> 회원가입 성공 메시지
   */
  @PostMapping("/signup")
  public ResponseEntity<ResponseMessage<String>> signup(@RequestBody UserSignupRequest request) {
    userService.signup(request);
    ResponseMessage<String> response = ResponseMessage.success("회원가입 성공", null);
    return ResponseEntity.ok(response);
  }

  /**
   * 회원탈퇴 처리
   * @param authentication 인증 정보 (현재 로그인한 사용자)
   * @return ResponseEntity<ResponseMessage<String>> 탈퇴 성공 메시지
   */
  @DeleteMapping("/delete")
  public ResponseEntity<ResponseMessage<String>> deleteUser(Authentication authentication) {
    String username = authentication.getName();
    userService.deleteUser(username);
    ResponseMessage<String> response = ResponseMessage.success("회원탈퇴 성공", null);
    return ResponseEntity.ok(response);
  }
}