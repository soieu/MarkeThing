package com.example.demo.auth.controller;

import com.example.demo.auth.dto.SignupDto;
import com.example.demo.auth.service.AuthService;
import com.example.demo.auth.service.PhoneAuthService;
import com.example.demo.config.SecurityConfig;
import com.example.demo.siteuser.entity.SiteUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(SecurityConfig.class)
@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @MockBean
    private MappingMongoConverter mongoConverter;

    @MockBean
    private AuthService authService;

    @MockBean
    private PhoneAuthService phoneAuthService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test

    public void SIGN_UP() throws Exception {
        // given
        String password = "123456";
        SignupDto signupDto = getSignUpDto();

        GeometryFactory geometryFactory = new GeometryFactory();
        double longitude = 126.97796919; // 경도
        double latitude = 37.56667062;   // 위도
        Point myLocation = geometryFactory.createPoint(new Coordinate(longitude, latitude));

        SiteUser siteUser = signupDto.toEntity(password, myLocation);

        String content = objectMapper.writeValueAsString(signupDto);

        given(authService.signUp(eq(signupDto))).willReturn(siteUser);

        // when & then
        mockMvc.perform(
                        post("/api/users/signUp").contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isOk()).andDo(print());
    }


    private static SignupDto getSignUpDto() {

        return SignupDto.builder().email("test@naver.com").password("password").name("test")
                .nickname("test").phoneNumber("010-1234-1234").address("address")
                .ProfileImg("ProfileImg").build();

    }


}
