package com.example.demo.auth.controller;

import com.example.demo.auth.dto.AuthCodeRequestDto;
import com.example.demo.auth.dto.PhoneNumberRequestDto;
import com.example.demo.auth.dto.SignupDto;
import com.example.demo.auth.dto.StringResponseDto;
import com.example.demo.auth.service.AuthService;
import com.example.demo.auth.service.PhoneAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final PhoneAuthService phoneAuthService;


    @PostMapping("/sign-up")
    public void signUp(@RequestBody SignupDto signupDto) {
        authService.signUp(signupDto);
    }

    @PostMapping("/auth/send-code")
    public ResponseEntity<StringResponseDto> sendCode(
            @RequestBody PhoneNumberRequestDto phoneNumberRequestDto) {
        var result = phoneAuthService.sendCode(phoneNumberRequestDto.getPhoneNumber());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/auth/verify-code")
    public ResponseEntity<StringResponseDto> verifyCode(
            @RequestBody AuthCodeRequestDto authCodeRequestDto) {
        var result = phoneAuthService.verifyCode(authCodeRequestDto.getPhoneNumber(),
                authCodeRequestDto.getAuthCode());
        return ResponseEntity.ok(result);
    }

}
