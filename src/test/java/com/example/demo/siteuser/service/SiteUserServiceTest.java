package com.example.demo.siteuser.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.demo.exception.MarkethingException;
import com.example.demo.exception.type.ErrorCode;
import com.example.demo.siteuser.dto.SiteUserResponseDto;
import com.example.demo.siteuser.entity.SiteUser;
import com.example.demo.siteuser.repository.SiteUserRepository;
import com.example.demo.siteuser.service.impl.SiteUserServiceImpl;
import com.example.demo.type.AuthType;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SiteUserServiceTest {

    @InjectMocks
    private SiteUserServiceImpl siteUserServiceImpl;

    @Mock
    private SiteUserRepository siteUserRepository;

    private static final GeometryFactory geometryFactory = new GeometryFactory();

    @Test
    @DisplayName("회원 삭제 성공 테스트")
    void successDeleteSiteUser() throws Exception {
        // given
        SiteUser siteUser = getSiteUser();
        lenient().when(siteUserRepository.findByEmail(siteUser.getEmail())).thenReturn(
                Optional.of(siteUser));
        // when
        siteUserServiceImpl.deleteSiteUser(siteUser.getEmail());
        // then
        verify(siteUserRepository,times(1)).delete(siteUser);
    }

    @Test
    @DisplayName("회원 삭제 실패 테스트 - USER NOT FOUND")
    void failDeleteSiteUser() throws Exception {
        // given
        SiteUser siteUser = getSiteUser();
        lenient().when(siteUserRepository.findByEmail(siteUser.getEmail())).thenReturn(
                Optional.empty());
        // when
        MarkethingException exception = assertThrows(MarkethingException.class,
                () -> siteUserServiceImpl.deleteSiteUser(siteUser.getEmail()));
        // then
        assertEquals(exception.getErrorCode(), ErrorCode.USER_NOT_FOUND);
    }

    @Test
    @DisplayName("내 정보 보여주기 성공 테스트")
    void successGetMyInformation() throws Exception {
        // given
        SiteUser siteUser = getSiteUser();
        given(siteUserRepository.findByEmail(siteUser.getEmail())).willReturn(Optional.of(siteUser));
        // when
        SiteUserResponseDto find = siteUserServiceImpl.getMyInformation(siteUser.getEmail());
        // then
        assertEquals(find.getEmail(), siteUser.getEmail());
        assertEquals(find.getName(), siteUser.getName());
    }

    @Test
    @DisplayName("내 정보 보여주기 실패 테스트 - USER NOT FOUND")
    void failGetMyInformation() throws Exception {
        SiteUser siteUser = getSiteUser();
        given(siteUserRepository.findByEmail(siteUser.getEmail())).willReturn(Optional.empty());
        // when
        MarkethingException exception = assertThrows(MarkethingException.class,
                () -> siteUserServiceImpl.getMyInformation(siteUser.getEmail()));
        // then
        assertEquals(exception.getErrorCode(), ErrorCode.USER_NOT_FOUND);
    }


    private static SiteUser getSiteUser() {
        return SiteUser.builder()
                .id(1L)
                .email("mockEmail@gmail.com")
                .password("password")
                .name("name")
                .nickname("nickname")
                .phoneNumber("010-1234-5678")
                .address("address")
                .myLocation(geometryFactory.createPoint(new Coordinate(37.56600357774501, 126.97306266269747)))
                .mannerScore(0)
                .profileImg("profileImg")
                .status(true)
                .authType(AuthType.GENERAL)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
