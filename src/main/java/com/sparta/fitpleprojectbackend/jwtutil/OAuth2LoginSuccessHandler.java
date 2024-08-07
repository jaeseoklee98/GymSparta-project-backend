package com.sparta.fitpleprojectbackend.jwtutil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  /**
   * OAuth2 로그인 성공 시 호출
   *
   * @param request        HTTP 요청 객체
   * @param response       HTTP 응답 객체
   * @param authentication 인증 객체
   * @throws IOException      입출력 예외
   * @throws ServletException 서블릿 예외
   */
  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {
    // 로그인 성공 후 추가 처리
    super.onAuthenticationSuccess(request, response, authentication);
  }
}