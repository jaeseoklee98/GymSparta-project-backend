package com.sparta.fitpleprojectbackend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.fitpleprojectbackend.common.CommonResponse;
import com.sparta.fitpleprojectbackend.jwtutil.JwtAuthenticationEntryPoint;
import com.sparta.fitpleprojectbackend.jwtutil.JwtAuthenticationFilter;
import com.sparta.fitpleprojectbackend.jwtutil.CustomAccessDeniedHandler;
import com.sparta.fitpleprojectbackend.jwtutil.JwtUtil;
import com.sparta.fitpleprojectbackend.oauth.CustomOauth2UserDetails;
import com.sparta.fitpleprojectbackend.oauth.CustomOauth2UserService;
import com.sparta.fitpleprojectbackend.security.CustomSessionExpiredStrategy;
import com.sparta.fitpleprojectbackend.security.UserDetailsServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final JwtAuthenticationEntryPoint unauthorizedHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final JwtUtil jwtUtil;

    public SecurityConfig(
            @Qualifier("userDetailsServiceImpl") UserDetailsServiceImpl userDetailsServiceImpl,
            JwtAuthenticationEntryPoint unauthorizedHandler,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            CustomAccessDeniedHandler accessDeniedHandler,
            JwtUtil jwtUtil) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.unauthorizedHandler = unauthorizedHandler;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.accessDeniedHandler = accessDeniedHandler;
        this.jwtUtil = jwtUtil;
    }


    /*
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("http://localhost:7070", "http://localhost:8080"));
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

     */


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
//                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .cors(cors -> cors
                        .configurationSource(CorsConfig.corsConfigurationSource())
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(unauthorizedHandler)
                        .accessDeniedHandler(accessDeniedHandler))
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
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

//        http.oauth2Login(oauth2 -> oauth2
//                .loginPage("/oauth-login/login")
//                .defaultSuccessUrl("/oauth-login")
//                .failureUrl("/oauth-login/login")
//                .permitAll()
                http.oauth2Login(oauth2 -> oauth2
                    .successHandler((request, response, authentication) -> {
                    String userEmail = (String) ((CustomOauth2UserDetails) authentication.getPrincipal()).getAttributes().get("email");

                    String accessToken = jwtUtil.generateAccessToken(userEmail);
                    String refreshToken = jwtUtil.generateRefreshToken(userEmail);

                    Map<String, String> tokenResponse = new HashMap<>();
                    tokenResponse.put("accessToken", accessToken);
                    tokenResponse.put("refreshToken", refreshToken);

                    CommonResponse<Map<String, String>> responseBody = new CommonResponse<>(
                            HttpStatus.OK.value(), "로그인 성공", tokenResponse);
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.setCharacterEncoding("UTF-8");

                    response.getWriter().write(new ObjectMapper().writeValueAsString(responseBody));
                    response.getWriter().flush();
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