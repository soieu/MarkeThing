package com.example.demo.siteuser.service;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.demo.siteuser.entity.SiteUser;
import com.example.demo.siteuser.repository.SiteUserRepository;
import com.example.demo.siteuser.service.impl.SiteUserServiceImpl;
import com.example.demo.type.AuthType;
import java.time.LocalDateTime;
import java.util.Optional;
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
    void success_delete_site_user() throws Exception {
        // given
        SiteUser siteUser = getSiteUser();
        lenient().when(siteUserRepository.findById(siteUser.getId())).thenReturn(
                Optional.of(siteUser));
        // when
        siteUserServiceImpl.deleteSiteUser(siteUser.getId());
        // then
        verify(siteUserRepository,times(1)).deleteById(siteUser.getId());
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
