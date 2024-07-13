package com.example.demo.auth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.demo.auth.sms.MessageSender;
import com.example.demo.exception.MarkethingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@ExtendWith(MockitoExtension.class)
public class PhoneAuthServiceTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private MessageSender messageSender;

    @InjectMocks
    private PhoneAuthService phoneAuthService;

    private static final String SMS_PREFIX = "sms";
    private final String phoneNumber = "01056979712";
    private final String randomNumber = "123456";

    @Test
    public void SEND_CODE() {
        //given
        ValueOperations<String, String> valueOperations = mock(ValueOperations.class);
        given(redisTemplate.opsForValue()).willReturn(valueOperations);

        //when
        var result = phoneAuthService.sendCode(phoneNumber);

        //then
        assertEquals("인증 번호가 전송되었습니다.", result.getMessage());
        verify(messageSender, times(1)).send(eq(phoneNumber), anyString());
    }

    @Test
    public void VERIFY_CODE_SUCCESS() {
        //given
        String key = SMS_PREFIX + ":" + phoneNumber;
        ValueOperations<String, String> valueOperations = mock(ValueOperations.class);
        given(redisTemplate.opsForValue()).willReturn(valueOperations);
        given(valueOperations.get(key)).willReturn(randomNumber);

        //when
        var result = phoneAuthService.verifyCode(phoneNumber, randomNumber);

        //then
        assertEquals("휴대폰 번호 인증이 완료되었습니다.", result.getMessage());
    }

    @Test
    public void VERIFY_CODE_FAIL_BY_CODE_EXPIRED() {
        //given
        String key = SMS_PREFIX + ":" + phoneNumber;
        ValueOperations<String, String> valueOperations = mock(ValueOperations.class);
        given(redisTemplate.opsForValue()).willReturn(valueOperations);
        given(valueOperations.get(key)).willReturn(null);

        //when
        MarkethingException exception = assertThrows(MarkethingException.class,
                () -> phoneAuthService.verifyCode(phoneNumber, randomNumber));

        //then
        assertEquals(exception.getMessage(), "휴대폰 인증 번호가 만료되었습니다.");
    }

    @Test
    public void VERIFY_CODE_FAIL_BY_CODE_DOES_NOT_MATCH() {
        //given
        String key = SMS_PREFIX + ":" + phoneNumber;
        ValueOperations<String, String> valueOperations = mock(ValueOperations.class);
        String wrongCode = "654321";
        given(redisTemplate.opsForValue()).willReturn(valueOperations);
        given(valueOperations.get(key)).willReturn(randomNumber);

        //when
        MarkethingException exception = assertThrows(MarkethingException.class,
                () -> phoneAuthService.verifyCode(phoneNumber, wrongCode));
        //then
        assertEquals(exception.getMessage(), "휴대폰 인증 번호가 일치하지 않습니다.");
    }
}


