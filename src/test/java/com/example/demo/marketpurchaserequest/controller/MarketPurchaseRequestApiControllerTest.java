package com.example.demo.marketpurchaserequest.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.auth.jwt.JWTFilter;
import com.example.demo.common.filter.dto.marketpurchaserequest.KeywordDto;
import com.example.demo.common.filter.dto.marketpurchaserequest.MarketFilterDto;
import com.example.demo.common.filter.dto.marketpurchaserequest.MarketFilterRequestDto;
import com.example.demo.common.filter.dto.marketpurchaserequest.MarketPurchaseRequestFilterDto;
import com.example.demo.common.filter.dto.marketpurchaserequest.MarketPurchaseRequestFilterRequestDto;
import com.example.demo.config.SecurityConfig;
import com.example.demo.marketpurchaserequest.controller.api.MarketPurchaseRequestApiController;
import com.example.demo.marketpurchaserequest.dto.DetailMarketPurchaseRequestDto;
import com.example.demo.marketpurchaserequest.dto.MarketPurchaseRequestDto;
import com.example.demo.marketpurchaserequest.dto.MarketPurchaseRequestPreviewDto;
import com.example.demo.marketpurchaserequest.dto.MarketResponseDto;
import com.example.demo.marketpurchaserequest.entity.Market;
import com.example.demo.marketpurchaserequest.entity.MarketPurchaseRequest;
import com.example.demo.marketpurchaserequest.service.MarketPurchaseRequestService;
import com.example.demo.siteuser.entity.SiteUser;
import com.example.demo.type.AuthType;
import com.example.demo.type.PurchaseRequestStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = MarketPurchaseRequestApiController.class)
@AutoConfigureMockMvc(addFilters = false)
public class MarketPurchaseRequestApiControllerTest {

    @MockBean
    private MarketPurchaseRequestService marketPurchaseRequestService;

    @MockBean
    private MappingMongoConverter mappingMongoConverter;

    @MockBean
    private SecurityConfig securityConfig;

    @MockBean
    private JWTFilter jwtFilter;

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
            .myLocation(geometryFactory
                    .createPoint(new Coordinate(37.56600357774501, 126.97306266269747)))
            .mannerScore(List.of("0,0,0"))
            .profileImg("profileImg")
            .status(true)
            .authType(AuthType.GENERAL)
            .createdAt(LocalDateTime.now())
            .build();

    private final Market market = Market.builder()
            .id(1L)
            .idNum("04003")
            .marketName("강릉중앙시장")
            .type(1)
            .roadAddress("강원특별자치도 강릉시 금성로21")
            .streetAddress("강원특별자치도 강릉시 성남동 50")
            .lon(128.8986233)
            .lat(37.75402359)
            .build();

    private final MarketPurchaseRequestDto marketPurchaseRequestDto =
            MarketPurchaseRequestDto.builder()
                    .title("test request")
                    .content("3 apples")
                    .fee(15000)
                    .meetupTime(LocalTime.now())
                    .meetupDate(LocalDate.now())
                    .meetupLat(37.5509)
                    .meetupLon(127.0506)
                    .marketId(market.getId())
                    .build();

    private final DetailMarketPurchaseRequestDto detailMarketPurchaseRequestDto =
            DetailMarketPurchaseRequestDto.builder()
                    .requestId(1L)
                    .title("title")
                    .content("content")
                    .postImg("postImg")
                    .fee(50000)
                    .meetupTime(LocalTime.now())
                    .meetupDate(LocalDate.now())
                    .meetupAddress("서울시")
                    .meetupLat(37.5509)
                    .meetupLon(127.0506)
                    .userId(siteUser.getId())
                    .marketId(market.getId())
                    .marketName(market.getMarketName())
                    .marketRoadAddress(market.getRoadAddress())
                    .marketStreetAddress(market.getStreetAddress())
                    .marketType(market.converMarketType())
                    .createdAt(LocalDateTime.now())
                    .build();

    private final MarketPurchaseRequestPreviewDto marketPurchaseRequestPreviewDto =
            MarketPurchaseRequestPreviewDto.builder()
                    .requestId(1L)
                    .title("title")
                    .content("content")
                    .fee(15000)
                    .meetupDate(LocalDate.now())
                    .meetupTime(LocalTime.now())
                    .marketName("marketName")
                    .nickname("nickname")
                    .build();

    private final MarketPurchaseRequestFilterRequestDto marketPurchaseRequestFilterRequestDto =
            MarketPurchaseRequestFilterRequestDto
                    .builder()
                    .filter(MarketPurchaseRequestFilterDto
                            .builder()
                            .purchaseRequestStatus(PurchaseRequestStatus.RECRUITING)
                            .meetupEndDt(null)
                            .meetupStartDt(null)
                            .build())
                    .build();

    private final MarketFilterRequestDto marketFilterRequestDto =
            MarketFilterRequestDto
                    .builder()
                    .filter(MarketFilterDto
                            .builder()
                            .sidoId("04")
                            .build())
                    .build();

    private final MarketResponseDto marketResponseDto =
            MarketResponseDto.fromEntity(market);

    @Test
    @DisplayName("시장 의뢰글 등록 테스트")
    void createMarketPurchaseRequest() throws Exception {
        // given
        String meetupAddress = "서울시 송파구";
        MarketPurchaseRequest marketPurchaseRequest = marketPurchaseRequestDto.toEntity(siteUser,
                market, meetupAddress);

        given(marketPurchaseRequestService.createMarketPurchaseRequest(any())).willReturn(
                marketPurchaseRequest);

        // when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/requests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(marketPurchaseRequestDto)));
        // then
        resultActions.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("시장 의뢰글 삭제 테스트")
    void deleteMarketPurchaseRequest() throws Exception {
        // given
        String meetupAddress = "서울시 송파구";
        String email = "mockEmail@gmail.com";
        MarketPurchaseRequest marketPurchaseRequest =
                marketPurchaseRequestDto.toEntity(siteUser, market, meetupAddress);

        given(marketPurchaseRequestService.createMarketPurchaseRequest(marketPurchaseRequestDto, email))
                .willReturn(marketPurchaseRequest);

        doNothing().when(marketPurchaseRequestService).deleteMarketPurchaseRequest(
                marketPurchaseRequest.getId(), email);

        // when & then
        mockMvc.perform(delete("/api/requests/1"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("시장 의뢰글 상세 조회")
    void getMarketPurchaseRequest() throws Exception {
        // given
        given(marketPurchaseRequestService.getMarketPurchaseRequest(1L))
                .willReturn(detailMarketPurchaseRequestDto);

        // when & then
        mockMvc.perform(get("/api/requests/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("시장 의뢰글 리스트 검색어 조회")
    void getMarketPurchaseRequestListByKeyword() throws Exception {
        // given
        KeywordDto keywordDto = new KeywordDto("keyword");
        String content = objectMapper.writeValueAsString(keywordDto);

        PageRequest pageRequest = PageRequest.of(0, 5, Sort.unsorted());
        List<MarketPurchaseRequestPreviewDto> requestPreviewDtos = new ArrayList<>();
        requestPreviewDtos.add(marketPurchaseRequestPreviewDto);

        Page<MarketPurchaseRequestPreviewDto> pages
                = new PageImpl<>(requestPreviewDtos, pageRequest, requestPreviewDtos.size());

        given(marketPurchaseRequestService.confirmSortOrder(eq("register")))
                .willReturn(Sort.by("createdAt").descending());

        given(marketPurchaseRequestService.getRequestsByKeyword(any(), any()))
                .willReturn(pages);

        // when & then
        mockMvc.perform(post("/api/requests/list/keyword")
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(5))
                        .param("sort", "register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].requestId")
                        .value(marketPurchaseRequestPreviewDto.getRequestId()))
                .andDo(print());
    }

    @Test
    @DisplayName("내 주변 시장 의뢰글 리스트 조회")
    void getMarketPurchaseRequestListAroundMe() throws Exception {
        // given
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.unsorted());
        List<MarketPurchaseRequestPreviewDto> requestPreviewDtos = new ArrayList<>();
        requestPreviewDtos.add(marketPurchaseRequestPreviewDto);

        Page<MarketPurchaseRequestPreviewDto> pages
                = new PageImpl<>(requestPreviewDtos, pageRequest, requestPreviewDtos.size());

        given(marketPurchaseRequestService.confirmSortOrder(eq("register")))
                .willReturn(Sort.by("createdAt").descending());

        given(marketPurchaseRequestService.getRequestsByKeyword(any(), any()))
                .willReturn(pages);

        // when & then
        mockMvc.perform(get("/api/requests/list/map")
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(5))
                        .param("distance", String.valueOf(3))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("시장 의뢰글 리스트 필터링 조회")
    void getMarketPurchaseRequestListByFilter() throws Exception {
        // given
        String content = objectMapper.writeValueAsString(marketPurchaseRequestFilterRequestDto);
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.unsorted());
        List<MarketPurchaseRequestPreviewDto> requestPreviewDtos = new ArrayList<>();
        requestPreviewDtos.add(marketPurchaseRequestPreviewDto);

        Page<MarketPurchaseRequestPreviewDto> pages
                = new PageImpl<>(requestPreviewDtos, pageRequest, requestPreviewDtos.size());

        given(marketPurchaseRequestService.confirmSortOrder(eq("register")))
                .willReturn(Sort.by("createdAt").descending());

        given(marketPurchaseRequestService.getRequestsByFilter(any(), any()))
                .willReturn(pages);

        // when & then
        mockMvc.perform(post("/api/requests/list")
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(5))
                        .param("sort", "register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].requestId")
                        .value(marketPurchaseRequestPreviewDto.getRequestId()))
                .andDo(print());
    }

    @Test
    @DisplayName("시장 리스트 필터링 조회")
    void getMarketListByFilter() throws Exception {
        // given
        String content = objectMapper.writeValueAsString(marketFilterRequestDto);
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.unsorted());
        List<MarketResponseDto> marketResponseDtos = new ArrayList<>();
        marketResponseDtos.add(marketResponseDto);

        Page<MarketResponseDto> pages
                = new PageImpl<>(marketResponseDtos, pageRequest, marketResponseDtos.size());

        given(marketPurchaseRequestService.confirmMarketSortOrder(eq("name")))
                .willReturn(Sort.by("marketName").ascending());

        given(marketPurchaseRequestService.getMarketsByFilter(any(), any()))
                .willReturn(pages);

        // when & then
        mockMvc.perform(post("/api/requests/markets/list")
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(5))
                        .param("sort", "name")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].idNum")
                        .value(marketResponseDto.getIdNum()))
                .andDo(print());
    }
}