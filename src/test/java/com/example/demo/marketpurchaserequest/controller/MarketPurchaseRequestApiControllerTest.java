package com.example.demo.marketpurchaserequest.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.market.entity.Market;
import com.example.demo.marketpurchaserequest.controller.api.MarketPurchaseRequestApiController;
import com.example.demo.marketpurchaserequest.dto.MarketPurchaseRequestDto;
import com.example.demo.marketpurchaserequest.entity.MarketPurchaseRequest;
import com.example.demo.marketpurchaserequest.service.MarketPurchaseRequestService;
import com.example.demo.siteuser.entity.SiteUser;
import com.example.demo.type.AuthType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(controllers = MarketPurchaseRequestApiController.class)
public class MarketPurchaseRequestApiControllerTest {

    @MockBean
    private MarketPurchaseRequestService marketPurchaseRequestService;

    @MockBean
    private MappingMongoConverter mappingMongoConverter;

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

    private final Market market = Market.builder()
            .id(1L)
            .idNum(04003)
            .marketName("강릉중앙시장")
            .type(1)
            .roadAddress("강원특별자치도 강릉시 금성로21")
            .streetAddress("강원특별자치도 강릉시 성남동 50")
            .location(geometryFactory.createPoint(new Coordinate(37.75402359, 128.8986233)))
            .build();

    @Test
    void createMarketPurchaseRequest() throws Exception {
        // given
        MarketPurchaseRequestDto marketPurchaseRequestDto = MarketPurchaseRequestDto.builder()
                .title("test request")
                .content("3 apples")
                .fee(15000)
                .meetupTime(LocalDate.now())
                .meetupDate(LocalDate.now())
                .meetupAddress("서울시")
                .latitude(37.5509)
                .longitude(127.0506)
                .userId(siteUser.getId())
                .marketId(market.getId())
                .build();

        MarketPurchaseRequest marketPurchaseRequest = marketPurchaseRequestDto.toEntity(siteUser,market);

        given(marketPurchaseRequestService.createMarketPurchaseRequest(any())).willReturn(marketPurchaseRequest);

        // when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/requests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(marketPurchaseRequestDto)));
        // then
        resultActions.andDo(print())
                .andExpect(status().isOk());
    }
}
