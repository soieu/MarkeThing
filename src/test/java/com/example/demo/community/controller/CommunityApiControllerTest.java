package com.example.demo.community.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.common.filter.dto.CommunityFilterDto;
import com.example.demo.common.filter.dto.CommunityFilterRequestDto;
import com.example.demo.community.controller.api.CommunityApiController;
import com.example.demo.community.dto.community.CommunityDetailDto;
import com.example.demo.community.dto.community.CommunityPreviewDto;
import com.example.demo.community.dto.community.CommunityRequestDto;
import com.example.demo.community.entity.Community;
import com.example.demo.community.service.CommunityService;
import com.example.demo.community.type.AreaType;
import com.example.demo.config.SecurityConfig;
import com.example.demo.siteuser.entity.SiteUser;
import com.example.demo.type.AuthType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(CommunityApiController.class)
@Import(SecurityConfig.class)
public class CommunityApiControllerTest {

    @MockBean
    private MappingMongoConverter mappingMongoConverter;
    @MockBean
    private CommunityService communityService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void createCommunityTest() throws Exception {
        // given
        CommunityRequestDto communityRequestDto = getCommunityRequestDto();
        Community community = communityRequestDto.toEntity(getSiteUser());

        String content = objectMapper.writeValueAsString(communityRequestDto);

        given(communityService.create(eq("mockEmail@gmail.com"), eq(communityRequestDto)))
                .willReturn(community);

        // when & then
        mockMvc.perform(post("/api/communities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)) // requestBody에 들어가는 인자 저장
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void editCommunityTest() throws Exception {
        // given
        CommunityRequestDto editCommunityRequestDto = getEditCommunityRequestDto();
        Community editCommunity = editCommunityRequestDto.toEntity(getSiteUser());

        String content = objectMapper.writeValueAsString(editCommunityRequestDto);

        given(communityService.edit(eq("mockEmail@gmail.com"), eq(editCommunityRequestDto)
                , eq(1L)))
                .willReturn(editCommunity);

        //when & then
        mockMvc.perform(post("/api/communities/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)) // requestBody에 들어가는 인자 저장
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void deleteCommunityTest() throws Exception {
        // when & then
        mockMvc.perform(delete("/api/communities/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void getCommunityListTest() throws Exception {
        //given
        CommunityFilterRequestDto communityFilterRequestDto = getCommunityFilterRequestDto();
        String content = objectMapper.writeValueAsString(communityFilterRequestDto);

        PageRequest pageRequest = PageRequest.of(0, 5, Sort.unsorted());
        List<CommunityPreviewDto> communityPreviewDtos = new ArrayList<>();
        CommunityPreviewDto communityPreviewDto = getCommunityPreviewDto();
        communityPreviewDtos.add(communityPreviewDto);

        Page<CommunityPreviewDto> pages
                = new PageImpl<>(communityPreviewDtos, pageRequest, communityPreviewDtos.size());


        given(communityService.confirmSortOrder(eq("date")))
                .willReturn(Sort.by("createdAt").descending());

        given(communityService.getCommunitiesByFilter(any(),
                any())) // 컨트롤러 내 서비스 단의 인자를 잡지 못래 any()로 대체
                .willReturn(pages);

        //when & then
        mockMvc.perform(post("/api/communities/list")
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(5))
                        .param("sort", "date")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)) // requestBody에 들어가는 인자 저장
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].area").value(
                        communityPreviewDto.getArea().toString()))
                .andExpect(jsonPath("$.content[0].title").value(communityPreviewDto.getTitle()))
                .andDo(print());
    }

    @Test
    public void getCommunityDetailTest() throws Exception {
        // given
        Community community = getCommunity();

        given(communityService.getCommunityDetail(eq(1L)))
                .willReturn(CommunityDetailDto.fromEntity(community));

        //when & then
        mockMvc.perform(get("/api/communities/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(community.getId()))
                .andExpect(jsonPath("$.area").value(community.getArea().toString()))
                .andDo(print());
    }

    @Test
    public void getMyCommunityListTest() throws Exception {
        //given
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.unsorted());
        List<CommunityPreviewDto> communityPreviewDtos = new ArrayList<>();
        CommunityPreviewDto communityPreviewDto = getCommunityPreviewDto();
        communityPreviewDtos.add(communityPreviewDto);

        Page<CommunityPreviewDto> pages
                = new PageImpl<>(communityPreviewDtos, pageRequest, communityPreviewDtos.size());

        given(communityService.confirmSortOrder(eq("date")))
                .willReturn(Sort.by("createdAt").descending());

        given(communityService.getMyCommunities(eq("mockEmail@gmail.com"),
                any())) // 컨트롤러 내 서비스 단의 인자를 잡지 못래 any()로 대체
                .willReturn(pages);

        //when & then
        mockMvc.perform(get("/api/communities/list/myList")
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(5))
                        .param("sort", "date")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].area").value(
                        communityPreviewDto.getArea().toString()))
                .andExpect(jsonPath("$.content[0].title").value(communityPreviewDto.getTitle()))
                .andDo(print());
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

    private static CommunityPreviewDto getCommunityPreviewDto() {
        return CommunityPreviewDto.builder()
                .id(1L)
                .nickname("nickname")
                .area(AreaType.SEOUL)
                .title("title")
                .build();
    }

    private static CommunityFilterRequestDto getCommunityFilterRequestDto() {
        return CommunityFilterRequestDto.builder()
                .filter(getFilterDto())
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
