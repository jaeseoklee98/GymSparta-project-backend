package com.sparta.fltpleprojectbackend.user.controller;

import com.sparta.fltpleprojectbackend.user.dto.ResponseMessage;
import com.sparta.fltpleprojectbackend.user.dto.UserSignupRequest;
import com.sparta.fltpleprojectbackend.user.entity.User;
import com.sparta.fltpleprojectbackend.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/signup")
  public ResponseEntity<ResponseMessage<User>> signup(@RequestBody UserSignupRequest request) {
    userService.signup(request);
    ResponseMessage<User> response = ResponseMessage.success("회원가입 성공");
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/profile/users/signout")
  public ResponseEntity<ResponseMessage<String>> deleteUser(Authentication authentication) {
    String username = authentication.getName();
    userService.deleteUser(username);
    ResponseMessage<String> response = ResponseMessage.success("회원탈퇴 성공");
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/profile/owners/signout")
  public ResponseEntity<ResponseMessage<String>> deleteOwner(Authentication authentication) {
    String username = authentication.getName();
    userService.deleteOwner(username);
    ResponseMessage<String> response = ResponseMessage.success("점주 탈퇴 성공");
    return ResponseEntity.ok(response);
  }
}