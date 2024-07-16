package com.example.demo.auth.controller;

import com.example.demo.auth.dto.AuthCodeRequestDto;
import com.example.demo.auth.dto.PhoneNumberRequestDto;
import com.example.demo.auth.dto.SignUpDto;
import com.example.demo.auth.dto.StringResponseDto;
import com.example.demo.auth.dto.UserInfoDto;
import com.example.demo.auth.service.AuthService;
import com.example.demo.auth.service.PhoneAuthService;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import com.example.demo.siteuser.entity.SiteUser;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
//    private final PrincipalOauthUserService principalOauthUserService;

    @PostMapping("/sign-up")
    public void signUp(@RequestBody SignUpDto signupDto)
            throws UnsupportedEncodingException, URISyntaxException {
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

    @PostMapping("/find-id")
    public ResponseEntity<StringResponseDto> findId(@RequestBody PhoneNumberRequestDto phoneNumberRequestDto){
        var result = new StringResponseDto(authService.findId(phoneNumberRequestDto.getPhoneNumber()).getEmail());
       return ResponseEntity.ok(result);
    }

    @PostMapping("/verify-user")
    public ResponseEntity<StringResponseDto> verifyUser(@RequestBody PhoneNumberRequestDto phoneNumberRequestDto){
        var result = authService.verifyUser(phoneNumberRequestDto);
        // 인증 번호 전송 후 일치하면 비밀번호 재설정 버튼을 활성화 시킬 것이다...
        return ResponseEntity.ok(result);
    }

    @PostMapping("/update-password")
    public ResponseEntity<StringResponseDto> updatePassword(@RequestBody Map<String, String> map){
        var result = authService.updatePassword(map);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<SiteUser> findUser(@PathVariable Long userId){
        var result = authService.findUser(userId);
        System.out.println("============="+result.toString()+"=============");
        return ResponseEntity.ok(result);
    }

}
