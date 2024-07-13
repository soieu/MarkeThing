package com.example.demo.siteuser.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import com.example.demo.exception.MarkethingException;
import com.example.demo.exception.type.ErrorCode;
import com.example.demo.siteuser.dto.SiteUserResponseDto;
import com.example.demo.siteuser.entity.SiteUser;
import com.example.demo.siteuser.repository.SiteUserRepository;
import com.example.demo.siteuser.service.impl.SiteUserServiceImpl;
import com.example.demo.type.AuthType;
import java.time.LocalDateTime;
import java.util.List;
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

    @Test
    @DisplayName("포인트 충전 성공 테스트")
    void testAccumulatePoint() {
        SiteUser siteUser = getSiteUser();

        when(siteUserRepository.findByEmail(siteUser.getEmail())).thenReturn(Optional.of(siteUser));

        siteUserServiceImpl.accumulatePoint(siteUser.getEmail(), 50);

        verify(siteUserRepository, times(1)).findByEmail(siteUser.getEmail());
        verify(siteUserRepository, times(1)).save(siteUser);

        assertEquals(25, siteUser.getPoint()); // 포인트가 올바르게 누적되었는지 확인
    }

    @Test
    @DisplayName("포인트 사용 성공 테스트")
    void testSpendPoint() {
        SiteUser siteUser = getSiteUser();

        when(siteUserRepository.findByEmail(siteUser.getEmail())).thenReturn(Optional.of(siteUser));

        siteUserServiceImpl.accumulatePoint(siteUser.getEmail(), 100);
        siteUserServiceImpl.spendPoint(siteUser.getEmail(), 25);

        verify(siteUserRepository, times(2)).findByEmail(siteUser.getEmail());
        verify(siteUserRepository, times(2)).save(siteUser);

        assertEquals(25, siteUser.getPoint()); // 포인트가 올바르게 차감되었는지 확인
    }

    @Test
    @DisplayName("포인트 충전 실패 테스트")
    void testAccumulatePointUserNotFound() {
        SiteUser siteUser = getSiteUser();

        when(siteUserRepository.findByEmail(siteUser.getEmail())).thenReturn(Optional.empty());

        assertThrows(MarkethingException.class, () -> siteUserServiceImpl.accumulatePoint(siteUser.getEmail(), 50));

        verify(siteUserRepository, times(1)).findByEmail(siteUser.getEmail());
        verify(siteUserRepository, times(0)).save(any(SiteUser.class));
    }

    @Test
    @DisplayName("포인트 사용 실패 테스트")
    void testSpendPointUserNotFound() {
        SiteUser siteUser = getSiteUser();

        when(siteUserRepository.findByEmail(siteUser.getEmail())).thenReturn(Optional.empty());

        assertThrows(MarkethingException.class, () -> siteUserServiceImpl.spendPoint(siteUser.getEmail(), 50));

        verify(siteUserRepository, times(1)).findByEmail(siteUser.getEmail());
        verify(siteUserRepository, times(0)).save(any(SiteUser.class));
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
                .point(0)
                .myLocation(geometryFactory.createPoint(new Coordinate(37.56600357774501, 126.97306266269747)))
                .mannerScore(List.of("0,0,0"))
                .profileImg("profileImg")
                .status(true)
                .authType(AuthType.GENERAL)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
