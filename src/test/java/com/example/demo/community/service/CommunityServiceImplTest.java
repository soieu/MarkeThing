package com.example.demo.community.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.demo.community.dto.CommunityRequestDto;
import com.example.demo.community.entity.Community;
import com.example.demo.community.repository.CommunityRepositoryCommunity;
import com.example.demo.community.service.impl.CommunityServiceImpl;
import com.example.demo.community.type.AreaType;
import com.example.demo.exception.MarkethingException;
import com.example.demo.exception.type.ErrorCode;
import com.example.demo.siteuser.entity.SiteUser;
import com.example.demo.siteuser.repository.SiteUserRepository;
import com.example.demo.type.AuthType;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CommunityServiceImplTest {

    @Mock
    private CommunityRepositoryCommunity communityRepository;

    @Mock
    private SiteUserRepository siteUserRepository;

    @InjectMocks
    private CommunityServiceImpl communityService;

    @Test
    void createSuccess() {
        // given
        SiteUser siteUser = getSiteUser();
        CommunityRequestDto communityRequestDto = getCommunityRequestDto();
        Community community = communityRequestDto.toEntity(siteUser);

        given(siteUserRepository.findByEmail(siteUser.getEmail()))
                .willReturn(Optional.of(siteUser));
        given(communityRepository.save(any(Community.class)))
                .willReturn(community);

        // when
        Community result = communityService.create(siteUser.getEmail(), communityRequestDto);

        // then
        assertThat(result.getSiteUser().getEmail()).isEqualTo(siteUser.getEmail());
    }

    @Test
    void createFailedByEmailNotFound() {
        // given
        given(siteUserRepository.findByEmail("mockEmail@gmail.com"))
                .willReturn(Optional.empty());

        // when
        MarkethingException exception = assertThrows(MarkethingException.class,
                () -> communityService.create("mockEmail@gmail.com", getCommunityRequestDto()));

        // then
        assertEquals(exception.getErrorCode(), ErrorCode.EMAIL_NOT_FOUND);
    }

    @Test
    void editSuccess() {
        // given
        SiteUser siteUser = getSiteUser();
        CommunityRequestDto communityRequestDto = getCommunityRequestDto();
        CommunityRequestDto editCommunityRequestDto = getEditCommunityRequestDto();
        Community community = communityRequestDto.toEntity(siteUser);

        given(siteUserRepository.findByEmail(siteUser.getEmail()))
                .willReturn(Optional.of(siteUser));
        given(communityRepository.findById(eq(1L)))
                .willReturn(Optional.of(community));

        // when
        Community result = communityService.edit(siteUser.getEmail(), editCommunityRequestDto, 1L);

        // then
        assertThat(result.getArea()).isEqualTo(editCommunityRequestDto.getArea());
        assertThat(result.getContent()).isEqualTo(editCommunityRequestDto.getContent());
        assertThat(result.getTitle()).isEqualTo(editCommunityRequestDto.getTitle());
        assertThat(result.getPostImg()).isEqualTo(editCommunityRequestDto.getPostImg());
    }

    @Test
    void editFailedByEmailNotFound() {
        // given
        given(siteUserRepository.findByEmail("mockEmail@gmail.com"))
                .willReturn(Optional.empty());

        // when
        MarkethingException exception = assertThrows(MarkethingException.class,
                () -> communityService.edit("mockEmail@gmail.com", getCommunityRequestDto(), 1L));

        // then
        assertEquals(exception.getErrorCode(), ErrorCode.EMAIL_NOT_FOUND);
    }

    @Test
    void editFailedByCommunityNotFound() {
        // given
        SiteUser siteUser = getSiteUser();

        given(siteUserRepository.findByEmail(siteUser.getEmail()))
                .willReturn(Optional.of(siteUser));
        given(communityRepository.findById(eq(1L)))
                .willReturn(Optional.empty());

        // when
        MarkethingException exception = assertThrows(MarkethingException.class,
                () -> communityService.edit("mockEmail@gmail.com", getCommunityRequestDto(), 1L));

        // then
        assertEquals(exception.getErrorCode(), ErrorCode.COMMUNITY_NOT_FOUND);
    }

    @Test
    void deleteSuccess() {
        // given
        String email = "test@example.com";
        Long communityId = 1L;
        SiteUser siteUser = getSiteUser();
        CommunityRequestDto communityRequestDto = getCommunityRequestDto();
        Community community = communityRequestDto.toEntity(siteUser);
        given(siteUserRepository.findByEmail(email))
                .willReturn(Optional.of(siteUser));
        given(communityRepository.findById(communityId))
                .willReturn(Optional.of(community));

        // when
        communityService.delete(email, communityId);

        // then
        verify(siteUserRepository, times(1)).findByEmail(email);
        verify(communityRepository, times(1)).findById(communityId);
        verify(communityRepository, times(1)).delete(community);
    }

    @Test
    void deleteFailedByEmailNotFound() {
        // given
        given(siteUserRepository.findByEmail("mockEmail@gmail.com"))
                .willReturn(Optional.empty());

        // when
        MarkethingException exception = assertThrows(MarkethingException.class,
                () -> communityService.delete("mockEmail@gmail.com", 1L));

        // then
        assertEquals(exception.getErrorCode(), ErrorCode.EMAIL_NOT_FOUND);
    }

    @Test
    void deleteFailedByCommunityNotFound() {
        // given
        SiteUser siteUser = getSiteUser();

        given(siteUserRepository.findByEmail(siteUser.getEmail()))
                .willReturn(Optional.of(siteUser));
        given(communityRepository.findById(eq(1L)))
                .willReturn(Optional.empty());

        // when
        MarkethingException exception = assertThrows(MarkethingException.class,
                () -> communityService.delete("mockEmail@gmail.com", 1L));

        // then
        assertEquals(exception.getErrorCode(), ErrorCode.COMMUNITY_NOT_FOUND);
    }

    private static SiteUser getSiteUser() {
        GeometryFactory geometryFactory = new GeometryFactory();
        double longitude = 126.97796919; // 경도
        double latitude = 37.56667062;   // 위도
        Point myLocation = geometryFactory.createPoint(new Coordinate(longitude, latitude));

        return SiteUser.builder()
                .id(1L)
                .email("mockEmail@gmail.com")
                .password("password")
                .name("name")
                .nickname("nickname")
                .phoneNumber("010-1234-5678")
                .address("address")
                .myLocation(myLocation)
                .mannerScore(0)
                .profileImg("profileImg")
                .status(true)
                .authType(AuthType.GENERAL)
                .createdAt(LocalDateTime.now())
                .build();
    }

    private static CommunityRequestDto getCommunityRequestDto() {
        return CommunityRequestDto
                .builder()
                .area(AreaType.SEOUL)
                .title("title")
                .content("content")
                .postImg("postImg")
                .build();
    }

    private static CommunityRequestDto getEditCommunityRequestDto() {
        return CommunityRequestDto
                .builder()
                .area(AreaType.GANGWON)
                .title("newTitle")
                .content("newContent")
                .postImg("newPostImg")
                .build();
    }
}
