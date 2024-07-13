package com.example.demo.siteuser.controller;


import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.auth.jwt.JWTFilter;
import com.example.demo.config.SecurityConfig;
import com.example.demo.siteuser.controller.api.SiteUserApiController;
import com.example.demo.siteuser.dto.MannerRequestDto;
import com.example.demo.siteuser.dto.SiteUserResponseDto;
import com.example.demo.siteuser.dto.UpdateSiteUserRequestDto;
import com.example.demo.siteuser.entity.SiteUser;
import com.example.demo.siteuser.service.MannerService;
import com.example.demo.siteuser.service.SiteUserService;
import com.example.demo.type.AuthType;
import com.example.demo.type.Rate;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(controllers = SiteUserApiController.class)
@Import({SecurityConfig.class})
@AutoConfigureMockMvc(addFilters = false)
public class SiteUserApiControllerTest {

    @MockBean
    private SiteUserService siteUserService;

    @MockBean
    private MannerService mannerService;

    @MockBean
    private MappingMongoConverter mongoConverter;

    @MockBean
    private SecurityConfig securityConfig;

    @MockBean
    private JWTFilter jwtFilter;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private final GeometryFactory geometryFactory = new GeometryFactory();

    private final SiteUser requester = SiteUser.builder()
            .id(1L)
            .email("mockEmail@gmail.com")
            .password("password")
            .name("name")
            .nickname("nickname")
            .phoneNumber("010-1234-5678")
            .address("address")
            .myLocation(geometryFactory.createPoint(new Coordinate(37.56600357774501, 126.97306266269747)))
            .mannerScore(List.of("0,0,0"))
            .profileImg("profileImg")
            .status(true)
            .authType(AuthType.GENERAL)
            .createdAt(LocalDateTime.now())
            .build();

    private final SiteUser agent = SiteUser.builder()
            .id(3L)
            .email("3mockEmail@gmail.com")
            .password("password")
            .name("name")
            .nickname("nick")
            .phoneNumber("010-2222-5678")
            .address("address")
            .myLocation(geometryFactory.createPoint(new Coordinate(37.56600357774501, 126.97306266269747)))
            .mannerScore(List.of("0,0,0"))
            .profileImg("profileImg")
            .status(true)
            .authType(AuthType.GENERAL)
            .createdAt(LocalDateTime.now())
            .build();


    private final MannerRequestDto mannerRequestDto = MannerRequestDto.builder()
            .rate(Rate.BAD).build();
    private final UpdateSiteUserRequestDto updateSiteUserRequestDto = UpdateSiteUserRequestDto.builder()
            .nickName("안녕")
            .phoneNumber("010-1234-5678")
            .address("광진구")
            .build();
    @Test
    @DisplayName("회원 삭제 테스트")
    void deleteSiteUser() throws Exception {
        // given
        doNothing().when(siteUserService).deleteSiteUser(requester.getEmail());
        // when & then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users"))
                .andExpect(status().isOk()).andDo(print());
    }

    @Test
    @DisplayName("내 정보 조회 테스트")
    void getMyInformation() throws Exception {
        String email = "mockEmail@gmail.com";
        // given
        when(siteUserService.getMyInformation(email)).thenReturn(
                SiteUserResponseDto.fromEntity(requester));
        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/information")).andExpect(status().isOk()).andDo(print());
    }
    @Test
    @DisplayName("회원 매너 점수 평가 테스트")
    void createManner() throws Exception {
        // given
        String email = "mockEmail@gmail.com";
        given(mannerService.createManner(mannerRequestDto,email,3L)).willReturn(mannerRequestDto.toEntity(requester,agent));
        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/3/manner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mannerRequestDto)))
                .andExpect(status().isOk()).andDo(print());
    }
    @Test
    @DisplayName("회원 정보 수정")
    void editSiteUser() throws Exception {
        // given
        String email = "mockEmail@gmail.com";
        doNothing().when(siteUserService).updateSiteUser(email, updateSiteUserRequestDto);
        // when&then
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateSiteUserRequestDto)))
                .andExpect(status().isOk()).andDo(print());
    }
}
