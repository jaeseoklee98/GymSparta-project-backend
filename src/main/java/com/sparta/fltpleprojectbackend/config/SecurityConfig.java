package com.sparta.fltpleprojectbackend.config;

import com.sparta.fltpleprojectbackend.jwtutil.JwtAuthenticationEntryPoint;
import com.sparta.fltpleprojectbackend.jwtutil.JwtAuthenticationFilter;
import com.sparta.fltpleprojectbackend.security.CustomSessionExpiredStrategy;
import com.sparta.fltpleprojectbackend.security.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final UserDetailsServiceImpl userDetailsServiceImpl;
  private final JwtAuthenticationEntryPoint unauthorizedHandler;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  // 생성자 주입
  public SecurityConfig(UserDetailsServiceImpl userDetailsServiceImpl,
                        JwtAuthenticationEntryPoint unauthorizedHandler,
                        JwtAuthenticationFilter jwtAuthenticationFilter) {
    this.userDetailsServiceImpl = userDetailsServiceImpl;
    this.unauthorizedHandler = unauthorizedHandler;
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
  }

  // Security 설정
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화
            .exceptionHandling(exceptionHandling -> exceptionHandling
                    .authenticationEntryPoint(unauthorizedHandler)) // 인증 실패 시 처리
            .sessionManagement(sessionManagement -> sessionManagement
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 정책을 STATELESS로 설정
                    .maximumSessions(1) // 최대 세션 수 설정
                    .expiredSessionStrategy(new CustomSessionExpiredStrategy()) // 세션 만료 시 실행될 전략 설정
                    .maxSessionsPreventsLogin(false))
            .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                    .requestMatchers("/api/login", "/api/user/signup", "/api/owner/signup", "/api/logout", "/api/profile/users/signout", "/api/profile/owners/signout").permitAll() // 특정 엔드포인트는 인증 없이 접근 허용
                    .anyRequest().authenticated()); // 나머지 모든 요청은 인증 필요

    http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // JWT 필터 추가

    return http.build();
  }

  // AuthenticationManager 빈 생성
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  // PasswordEncoder 빈 생성
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}