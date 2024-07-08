package com.example.demo.config;

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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final AuthenticationConfiguration authenticationConfiguration;

  public SecurityConfig(AuthenticationConfiguration authenticationConfiguration) {
    this.authenticationConfiguration = authenticationConfiguration;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

    return configuration.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {

    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http
      .csrf((auth) -> auth
        .disable());

    http
      .formLogin((auth) -> auth
        .disable());

    http
      .httpBasic((auth) -> auth
        .disable());

    http
      .authorizeHttpRequests((auth) -> auth
        .requestMatchers(
          new AntPathRequestMatcher("/"),
          new AntPathRequestMatcher("/login"),
          new AntPathRequestMatcher("/api/users/signUp")
        ).permitAll()
        .anyRequest()
        .authenticated());

    http
      .sessionManagement((session) -> session
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    return http.build();
  }
}
