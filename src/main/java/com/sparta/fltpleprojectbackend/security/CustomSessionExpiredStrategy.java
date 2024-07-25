package com.sparta.fltpleprojectbackend.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import java.io.IOException;

public class CustomSessionExpiredStrategy implements SessionInformationExpiredStrategy {

  @Override
  public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException {
    HttpServletResponse response = event.getResponse();
    response.setContentType("application/json;charset=UTF-8");
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.getWriter().write("{\"message\": \"세션이 만료되었습니다. 다시 로그인 해주세요.\"}");
  }
}