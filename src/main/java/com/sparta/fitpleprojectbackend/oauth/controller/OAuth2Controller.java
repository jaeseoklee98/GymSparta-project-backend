package com.sparta.fitpleprojectbackend.oauth.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/oauth")
public class OAuth2Controller {

  @GetMapping("/success")
  public String success(@AuthenticationPrincipal OAuth2User principal) {
    // principal.getAttributes() 를 사용하여 사용자 정보를 가져옵니다.
    return "로그인 성공: " + principal.getAttributes();
  }

  @GetMapping("/failure")
  public String failure() {
    return "로그인 실패";
  }
}