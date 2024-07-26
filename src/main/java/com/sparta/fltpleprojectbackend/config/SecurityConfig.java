package com.sparta.fltpleprojectbackend.config;

import com.sparta.fltpleprojectbackend.jwtutil.JwtAuthenticationEntryPoint;
import com.sparta.fltpleprojectbackend.jwtutil.JwtAuthenticationFilter;
import com.sparta.fltpleprojectbackend.security.CustomSessionExpiredStrategy;
import com.sparta.fltpleprojectbackend.security.UserDetailsServiceImpl;
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

  public SecurityConfig(
      @Qualifier("userDetailsServiceImpl") UserDetailsServiceImpl userDetailsServiceImpl,
      JwtAuthenticationEntryPoint unauthorizedHandler,
      JwtAuthenticationFilter jwtAuthenticationFilter) {
    this.userDetailsServiceImpl = userDetailsServiceImpl;
    this.unauthorizedHandler = unauthorizedHandler;
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .exceptionHandling(exceptionHandling -> exceptionHandling
            .authenticationEntryPoint(unauthorizedHandler))
        .sessionManagement(sessionManagement -> sessionManagement
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .maximumSessions(1)
            .expiredSessionStrategy(new CustomSessionExpiredStrategy())
            .maxSessionsPreventsLogin(false))
        .authorizeHttpRequests(authorizeRequests -> authorizeRequests
            .requestMatchers("/api/login", "/api/user/signup", "/api/owners/signup", "/api/logout", "/api/profile/users/signout", "/api/profile/owners/signout", "/error").permitAll()
            .requestMatchers("/api/profile/users/**").hasRole("USER")
            .requestMatchers("/api/profile/trainers/**").hasRole("TRAINER")
            .requestMatchers("/api/profile/owners/**").hasRole("OWNER")
            .anyRequest().authenticated());

    http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

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