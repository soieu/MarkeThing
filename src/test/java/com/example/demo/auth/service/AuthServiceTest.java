package com.example.demo.auth.service;

import com.example.demo.auth.dto.SignupDto;
import com.example.demo.exception.MarkethingException;
import com.example.demo.exception.type.ErrorCode;
import com.example.demo.siteuser.entity.SiteUser;
import com.example.demo.siteuser.repository.SiteUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private SiteUserRepository siteUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    public void SIGN_UP_SUCCESS() {

        // given
        GeometryFactory geometryFactory = new GeometryFactory();
        double longitude = 126.97796919; // 경도
        double latitude = 37.56667062;   // 위도
        Point myLocation = geometryFactory.createPoint(new Coordinate(longitude, latitude));

        String password = "123456";
        SignupDto signupDto = getSignUpDto();
        SiteUser siteUser = signupDto.toEntity(password, myLocation);

        given(siteUserRepository.existsByEmail(siteUser.getEmail()))
                .willReturn(false);

        given(siteUserRepository.save(any(SiteUser.class)))
                .willReturn(siteUser);

        ArgumentCaptor<SiteUser> captor = ArgumentCaptor.forClass(SiteUser.class);

        // when
        SiteUser result = authService.signUp(signupDto);

        // then
        verify(siteUserRepository, times(1)).save(captor.capture());

    }

    @Test
    public void SIGN_UP_FAILED_BY_ALREADY_EXISTED_EMAIL() {
        // given
        given(siteUserRepository.existsByEmail(getSignUpDto().getEmail()))
                .willReturn(true);

        ArgumentCaptor<SiteUser> captor = ArgumentCaptor.forClass(SiteUser.class);

        // when
        MarkethingException exception = assertThrows(MarkethingException.class,
                () -> authService.signUp(getSignUpDto()));

        // then
        assertEquals(exception.getErrorCode(), ErrorCode.EMAIL_ALREADY_EXISTED);

    }

    private static SignupDto getSignUpDto() {

        return SignupDto
                .builder()
                .email("test@naver.com")
                .password("password")
                .name("test")
                .nickname("test")
                .phoneNumber("010-1234-1234")
                .address("address")
                .ProfileImg("ProfileImg")
                .build();

    }
}
