package com.example.demo.auth.service;

import com.example.demo.auth.sms.MessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PhoneAuthService {

    private final static String SMS_PREFIX = "sms";
    private final static String SMS_SEND_OK = "인증 번호가 전송되었습니다.";
    private final static String SMS_VERIFICATION_OK = "휴대폰 번호 인증이 완료되었습니다.";

    private final RedisTemplate<String, String> redisTemplate;
    private final MessageSender messageSender;

//  public String sendCode(String PhoneNumber){
//
//  }

}
