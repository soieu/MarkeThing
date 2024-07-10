package com.example.demo.siteuser.controller;


import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.siteuser.controller.api.SiteUserApiController;
import com.example.demo.siteuser.entity.SiteUser;
import com.example.demo.siteuser.service.SiteUserService;
import com.example.demo.type.AuthType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(controllers = SiteUserApiController.class)
public class SiteUserApiControllerTest {

    @MockBean
    private SiteUserService siteUserService;

    @MockBean
    private MappingMongoConverter mongoConverter;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private final GeometryFactory geometryFactory = new GeometryFactory();

    private final SiteUser siteUser = SiteUser.builder()
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

    @Test
    @WithMockUser
    void deleteSiteUser() throws Exception {
        // given
        doNothing().when(siteUserService).deleteSiteUser(siteUser.getEmail());
        // when & then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users").with(csrf()))
                .andExpect(status().isOk()).andDo(print());
    }

}
