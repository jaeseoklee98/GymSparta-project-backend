package com.sparta.fitpleprojectbackend.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import java.io.IOException;

public class CustomSessionExpiredStrategy implements SessionInformationExpiredStrategy {

  /**
   * 세션 만료가 감지되었을 때 호출되는 메서드
   * 만료된 세션에 대한 적절한 응답을 클라이언트에 전송
   *
   * @param event 세션 만료 이벤트
   * @throws IOException 입출력 예외
   */
  @Override
  public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException {
    HttpServletResponse response = event.getResponse();
    response.setContentType("application/json;charset=UTF-8");
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.getWriter().write("{\"message\": \"세션이 만료되었습니다. 다시 로그인 해주세요.\"}");
  }
}