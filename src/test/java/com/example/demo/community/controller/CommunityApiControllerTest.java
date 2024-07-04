package com.example.demo.community.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.community.dto.CommunityRequestDto;
import com.example.demo.community.entity.Community;
import com.example.demo.community.service.CommunityService;
import com.example.demo.siteuser.entity.SiteUser;
import com.example.demo.type.AuthType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CommunityApiController.class)
// @Import(SecurityConfiguration.class) // 로그인 api 작성 후 주석 제거
public class CommunityApiControllerTest {

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

        //when & then
        mockMvc.perform(post("/api/communities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)) // requestBody에 들어가는 인자 저장
                .andExpect(status().isOk())
                .andDo(print());
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
                .area("area")
                .title("title")
                .content("content")
                .postImg("postImg")
                .build();
    }
}
