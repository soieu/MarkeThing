package com.example.demo.auth.service;

import com.example.demo.auth.dto.StringResponseDto;
import com.example.demo.auth.sms.MessageSender;
import java.util.Random;
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

    public StringResponseDto sendCode(String PhoneNumber) {
        String randomNumber = makeRandom();
        return new StringResponseDto(SMS_SEND_OK);
    }

    private String makeMessage(String phoneNumber) {
        return "[Markething] 인증번호는 " + phoneNumber + "입니다. (5분 안에 입력해 주세요!)";
    }

    private String makeRandom(){
        StringBuilder random = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < 6; i++) {
            String randNum = Integer.toString(rand.nextInt(10));
            random.append(randNum);
        }
        return random.toString();
    }

}
