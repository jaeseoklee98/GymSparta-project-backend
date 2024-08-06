package com.sparta.fitpleprojectbackend.config;

import com.sparta.fitpleprojectbackend.jwtutil.JwtAuthenticationEntryPoint;
import com.sparta.fitpleprojectbackend.jwtutil.JwtAuthenticationFilter;
import com.sparta.fitpleprojectbackend.jwtutil.CustomAccessDeniedHandler;
import com.sparta.fitpleprojectbackend.security.CustomSessionExpiredStrategy;
import com.sparta.fitpleprojectbackend.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
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
  private final CustomAccessDeniedHandler accessDeniedHandler;

  public SecurityConfig(
      @Qualifier("userDetailsServiceImpl") UserDetailsServiceImpl userDetailsServiceImpl,
      JwtAuthenticationEntryPoint unauthorizedHandler,
      JwtAuthenticationFilter jwtAuthenticationFilter,
      CustomAccessDeniedHandler accessDeniedHandler) {
    this.userDetailsServiceImpl = userDetailsServiceImpl;
    this.unauthorizedHandler = unauthorizedHandler;
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    this.accessDeniedHandler = accessDeniedHandler;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
        .exceptionHandling(exceptionHandling -> exceptionHandling
            .authenticationEntryPoint(unauthorizedHandler)
            .accessDeniedHandler(accessDeniedHandler))
        .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .maximumSessions(1)
            .expiredSessionStrategy(new CustomSessionExpiredStrategy())
            .maxSessionsPreventsLogin(false))
        .authorizeHttpRequests(authorizeRequests -> authorizeRequests
            .requestMatchers("/api/login", "/api/user/signup", "/api/owners/signup", "/api/logout", "/api/profile/users/signout",
                "/api/profile/owners/signout", "/api/trainers", "/error", "/favicon.ico", "/oauth2/**")
            .permitAll()
            .requestMatchers("/api/profile/users/**").hasRole("USER")
            .requestMatchers("/api/profile/trainers/**").hasRole("TRAINER")
            .requestMatchers("/api/profile/owners/**").hasRole("OWNER")
            .requestMatchers("/api/stores/**").permitAll()
            .requestMatchers("/api/stores/admin/**").hasAuthority("OWNER")
            .requestMatchers("/api/ptpayments/complete").authenticated()
            .anyRequest().authenticated());

    http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    http.oauth2Login(oauth2 -> oauth2
        .successHandler((request, response, authentication) -> {
          // OAuth2 로그인 성공 후에 호출되는 핸들러
          // 여기서 인증된 사용자의 정보를 처리할 수 있습니다.
          System.out.println("OAuth2 로그인 성공");
          response.sendRedirect("/"); // 로그인 성공 후에 리다이렉트할 URL
        })
    );

    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}