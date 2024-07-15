package com.example.demo.community.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.demo.common.filter.dto.community.CommunityFilterDto;
import com.example.demo.community.dto.community.CommunityRequestDto;
import com.example.demo.community.entity.Community;
import com.example.demo.community.repository.CommunityRepository;
import com.example.demo.community.service.impl.CommunityServiceImpl;
import com.example.demo.community.type.AreaType;
import com.example.demo.exception.MarkethingException;
import com.example.demo.exception.type.ErrorCode;
import com.example.demo.siteuser.entity.SiteUser;
import com.example.demo.siteuser.repository.SiteUserRepository;
import com.example.demo.type.AuthType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
public class CommunityServiceImplTest {

    @Mock
    private CommunityRepository communityRepository;

    @Mock
    private SiteUserRepository siteUserRepository;

    @InjectMocks
    private CommunityServiceImpl communityService;

    @Test
    void createSuccess() {
        // given
        SiteUser siteUser = getSiteUser(); // 사용자를 지정함
        CommunityRequestDto communityRequestDto = getCommunityRequestDto(); // dto도!
        Community community = communityRequestDto.toEntity(siteUser); //dto로 부터 community를 찾겠지

        given(siteUserRepository.findByEmail(siteUser.getEmail())) //
                .willReturn(Optional.of(siteUser));
        given(communityRepository.save(any(Community.class)))
                .willReturn(community);

        // when
        Community result = communityService.create(siteUser.getEmail(), communityRequestDto);

        // then
        assertThat(result.getSiteUser().getEmail()).isEqualTo(siteUser.getEmail());
    }

    @Test
    void createFailedByUserNotFound() {
        // given
        given(siteUserRepository.findByEmail("mockEmail@gmail.com"))
                .willReturn(Optional.empty());

        // when
        MarkethingException exception = assertThrows(MarkethingException.class,
                () -> communityService.create("mockEmail@gmail.com", getCommunityRequestDto()));
        // then
        assertEquals(exception.getErrorCode(), ErrorCode.USER_NOT_FOUND);
    }

    @Test
    void editSuccess() {
        // given
        SiteUser siteUser = getSiteUser();
        CommunityRequestDto communityRequestDto = getCommunityRequestDto();
        CommunityRequestDto editedCommunityRequestDto = getEditedCommunityRequestDto();
        Community community = communityRequestDto.toEntity(siteUser);

        given(siteUserRepository.findByEmail(siteUser.getEmail()))
                .willReturn(Optional.of(siteUser));
        given(communityRepository.findById(eq(1L)))
                .willReturn(Optional.of(community));

        // when
        Community result = communityService.edit(siteUser.getEmail(), editedCommunityRequestDto, 1L);

        // then
        assertThat(result.getArea()).isEqualTo(editedCommunityRequestDto.getArea());
        assertThat(result.getContent()).isEqualTo(editedCommunityRequestDto.getContent());
        assertThat(result.getTitle()).isEqualTo(editedCommunityRequestDto.getTitle());
        assertThat(result.getPostImg()).isEqualTo(editedCommunityRequestDto.getPostImg());
    }

    @Test
    void editFailedByUserNotFound() {
        // given
        given(siteUserRepository.findByEmail("mockEmail@gmail.com"))
                .willReturn(Optional.empty());

        // when
        MarkethingException exception = assertThrows(MarkethingException.class,
                () -> communityService.edit("mockEmail@gmail.com", getCommunityRequestDto(), 1L));

        // then
        assertEquals(exception.getErrorCode(), ErrorCode.USER_NOT_FOUND);
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
    void deleteFailedByUserNotFound() {
        // given
        given(siteUserRepository.findByEmail("mockEmail@gmail.com"))
                .willReturn(Optional.empty());

        // when
        MarkethingException exception = assertThrows(MarkethingException.class,
                () -> communityService.delete("mockEmail@gmail.com", 1L));

        // then
        assertEquals(exception.getErrorCode(), ErrorCode.USER_NOT_FOUND);
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

    @Test
    void getCommunitiesByFilter() {
        // given
        CommunityFilterDto communityFilterDto = getFilterDto();
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.unsorted());

        Community community = getCommunity();
        List<Community> communities = new ArrayList<>();
        communities.add(community);

        PageImpl<Community> communityPage
                = new PageImpl<>(communities, pageRequest, communities.size());

        given(communityRepository.findAllByFilter(any(), any()))
                .willReturn(communityPage);

        // when
        var result = communityService.getCommunitiesByFilter(communityFilterDto, pageRequest);

        // then
        assertThat(result.getContent().get(0).getArea()).isEqualTo(community.getArea());
        assertThat(result.getContent().get(0).getTitle()).isEqualTo(community.getTitle());
    }

    @Test
    void getCommunityDetail() {
        // given
        Community community = getCommunity();
        given(communityRepository.findById(eq(community.getId())))
        .willReturn(Optional.of(community));

        // when
        var result = communityService.getCommunityDetail(community.getId());

        //then
        assertThat(result.getArea()).isEqualTo(community.getArea());
        assertThat(result.getContent()).isEqualTo(community.getContent());
    }

    @Test
    void getMyCommunities() {
        // given
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.unsorted());

        Community community = getCommunity();
        List<Community> communities = new ArrayList<>();
        communities.add(community);

        PageImpl<Community> communityPage
                = new PageImpl<>(communities, pageRequest, communities.size());

        given(communityRepository.findAllBySiteUser_email(eq("mockEmail@gmail.com"), any()))
                .willReturn(communityPage);

        // when
        var result = communityService.getMyCommunities("mockEmail@gmail.com", pageRequest);

        // then
        assertThat(result.getContent().get(0).getArea()).isEqualTo(community.getArea());
        assertThat(result.getContent().get(0).getTitle()).isEqualTo(community.getTitle());
    }

    private static Community getCommunity() {
        return Community.builder()
                .siteUser(getSiteUser())
                .id(1L)
                .area(AreaType.SEOUL)
                .title("title")
                .content("content")
                .postImg("postImg")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private static CommunityFilterDto getFilterDto() {
        List<AreaType> areaTypes = new ArrayList<>();
        areaTypes.add(AreaType.SEOUL);
        return CommunityFilterDto.builder()
                .areas(areaTypes)
                .build();
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
                .mannerScore(List.of("0,0,0"))
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

    private static CommunityRequestDto getEditedCommunityRequestDto() {
        return CommunityRequestDto
                .builder()
                .area(AreaType.GANGWON)
                .title("newTitle")
                .content("newContent")
                .postImg("newPostImg")
                .build();
    }
}
