package com.example.demo.siteuser.controller;


import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.auth.jwt.JWTFilter;
import com.example.demo.config.SecurityConfig;
import com.example.demo.siteuser.controller.api.SiteUserApiController;
import com.example.demo.siteuser.dto.MannerRequestDto;
import com.example.demo.siteuser.dto.PointDto;
import com.example.demo.siteuser.dto.SiteUserResponseDto;
import com.example.demo.siteuser.entity.SiteUser;
import com.example.demo.siteuser.repository.SiteUserRepository;
import com.example.demo.siteuser.service.MannerService;
import com.example.demo.siteuser.service.SiteUserService;
import com.example.demo.type.AuthType;
import com.example.demo.type.Rate;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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

    @MockBean
    private SiteUserRepository siteUserRepository;

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

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

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
        // given
        given(siteUserService.getMyInformation("mockEmail@gmail.com")).willReturn(
                SiteUserResponseDto.fromEntity(requester));
        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/information")).andExpect(status().isOk()).andExpect(jsonPath("$.email").value("mockEmail@gmail.com")).andDo(print());
    }
    @Test
    @DisplayName("회원 매너 점수 평가 테스트")
    void createManner() throws Exception {
        // given
        String email = "mockEmail@gmail.com";
        given(mannerService.createManner(mannerRequestDto,email,3L)).willReturn(mannerRequestDto.toEntity(requester,agent));
        // when & then
        mockMvc.perform(post("/api/users/3/manner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mannerRequestDto)))
                .andExpect(status().isOk()).andDo(print());
    }

    @Test
    @DisplayName("포인트 충전 테스트")
    public void testAccumulatePoint() throws Exception {
        String mockUsername = "mockUser";

        PointDto pointDto = new PointDto(100);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/point/accumulate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\": 100}")  // JSON representation of PointDto
                        .principal(() -> mockUsername)) // Injecting the mock Principal
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Verify that the service method was called
        Mockito.verify(siteUserService, Mockito.times(1)).accumulatePoint(mockUsername, pointDto.getAmount());
    }

    @Test
    public void testSpendPoint() throws Exception {
        String mockUsername = "mockUser";

        PointDto pointDto = new PointDto(50);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/point/spend")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\": 50}")  // JSON representation of PointDto
                        .principal(() -> mockUsername)) // Injecting the mock Principal
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(siteUserService, Mockito.times(1)).spendPoint(mockUsername, pointDto.getAmount());
    }
}
