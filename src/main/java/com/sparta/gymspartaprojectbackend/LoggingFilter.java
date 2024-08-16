package com.sparta.gymspartaprojectbackend;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoggingFilter implements Filter {

  private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse res = (HttpServletResponse) response;

    logger.debug("Request  {} : {}", req.getMethod(), req.getRequestURI());
    chain.doFilter(request, response);
    logger.debug("Response :{}", res.getContentType());
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    // 필터 초기화 로직 (필요 시)
  }

  @Override
  public void destroy() {
    // 필터 종료 로직 (필요 시)
  }
}