package com.example.demo.auth.service;

import com.example.demo.auth.dto.StringResponseDto;
import com.example.demo.auth.sms.MessageSender;
import com.example.demo.exception.MarkethingException;
import com.example.demo.exception.type.ErrorCode;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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

    public StringResponseDto sendCode(String phoneNumber) {
        String randomNumber = makeRandom();
        messageSender.send(phoneNumber, makeMessage(randomNumber));
        redisTemplate.opsForValue()
                .set(SMS_PREFIX + ":" + phoneNumber, randomNumber, 5, TimeUnit.MINUTES);
        return new StringResponseDto(SMS_SEND_OK);
    }

    public StringResponseDto verifyCode(String phoneNumber, String authCode) {
        String key = SMS_PREFIX + ":" + phoneNumber;
        String code = redisTemplate.opsForValue().get(key);
        if (StringUtils.isBlank(code)){
            throw new MarkethingException(ErrorCode.PHONE_AUTH_NUM_EXPIRED);
        }
        if (code.equals(authCode)) {
            redisTemplate.delete(key);
            return new StringResponseDto(SMS_VERIFICATION_OK);
        }
        throw new MarkethingException(ErrorCode.PHONE_AUTH_NUM_DOESNT_MATCH);
    }

    private String makeMessage(String randomNumber) {
        return "[Markething] 인증번호는 " + randomNumber + "입니다. (5분 안에 입력해 주세요!)";
    }

    private String makeRandom() {
        StringBuilder random = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < 5; i++) {
            String randNum = Integer.toString(rand.nextInt(10));
            random.append(randNum);
        }
        return random.toString();
    }

}
