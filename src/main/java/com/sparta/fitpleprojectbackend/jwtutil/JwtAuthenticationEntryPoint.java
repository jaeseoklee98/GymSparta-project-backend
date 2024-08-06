package com.sparta.fitpleprojectbackend.jwtutil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  /**
   * 인증되지 않은 사용자가 보호된 리소스에 접근할 때 호출
   *
   * @param request       HTTP 요청 객체
   * @param response      HTTP 응답 객체
   * @param authException 발생한 인증 예외
   * @throws IOException 입출력 예외
   */
  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException {
    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
  }
}