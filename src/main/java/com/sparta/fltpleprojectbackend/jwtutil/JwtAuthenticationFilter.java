package com.sparta.fltpleprojectbackend.jwtutil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  @Qualifier("userDetailsServiceImpl")
  private UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {

    final String authHeader = request.getHeader("Authorization");
    String username = null;
    String jwt = null;

    // 점주 회원가입 경로는 예외 처리
    String path = request.getRequestURI();
    if (path.equals("/api/owners/signup")) { // 정확한 경로로 수정
      chain.doFilter(request, response);
      return;
    }

    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      jwt = authHeader.substring(7);
      username = jwtUtil.getUsername(jwt);
    }

    if (username != null && (SecurityContextHolder.getContext().getAuthentication() == null || !SecurityContextHolder.getContext().getAuthentication().isAuthenticated())) {
      UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

      if (jwtUtil.validateToken(jwt)) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
      }
    }
    chain.doFilter(request, response);
  }
}