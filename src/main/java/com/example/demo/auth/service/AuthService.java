package com.example.demo.auth.service;

import com.example.demo.auth.dto.SignupDto;
import com.example.demo.siteuser.entity.SiteUser;
import com.example.demo.siteuser.repository.SiteUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final SiteUserRepository siteUserRepository;
  private final PasswordEncoder passwordEncoder;

  public SiteUser signUp(SignupDto signupDto) {

    boolean isExists = siteUserRepository.existsByEmail(signupDto.getEmail());

    if (isExists) {
      return null;
    }

    var result = signupDto.toEntity(passwordEncoder.encode(signupDto.getPassword()));
    return siteUserRepository.save(result);

  }


}
