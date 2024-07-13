package com.example.demo.marketpurchaserequest.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.demo.common.filter.dto.KeywordDto;
import com.example.demo.exception.MarkethingException;
import com.example.demo.exception.type.ErrorCode;
import com.example.demo.market.entity.Market;
import com.example.demo.market.repository.MarketRepository;
import com.example.demo.marketpurchaserequest.dto.MarketPurchaseRequestDto;
import com.example.demo.marketpurchaserequest.dto.MarketPurchaseRequestPreviewDto;
import com.example.demo.marketpurchaserequest.entity.MarketPurchaseRequest;
import com.example.demo.marketpurchaserequest.repository.MarketPurchaseRequestRepository;
import com.example.demo.marketpurchaserequest.service.impl.MarketPurchaseRequestServiceImpl;
import com.example.demo.siteuser.entity.SiteUser;
import com.example.demo.siteuser.repository.SiteUserRepository;
import com.example.demo.type.AuthType;
import com.example.demo.type.PurchaseRequestStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
public class MarketPurchaseRequestServiceImplTest {

    @InjectMocks
    private MarketPurchaseRequestServiceImpl marketPurchaseRequestServiceImpl;

    @Mock
    private MarketPurchaseRequestRepository marketPurchaseRequestRepository;

    @Mock
    private SiteUserRepository siteUserRepository;

    @Mock
    private MarketRepository marketRepository;

    private static final GeometryFactory geometryFactory = new GeometryFactory();

    @Test
    @DisplayName("시장 의뢰글 등록 성공 테스트")
    void createMarketPurchaseRequest() throws Exception {
        //given
        Market market = getMarket();
        SiteUser siteUser = getSiteUser();
        MarketPurchaseRequestDto marketPurchaseRequestDto = getMarketPurchaseRequestDto(siteUser,
                market);
        MarketPurchaseRequest marketPurchaseRequest = marketPurchaseRequestDto.toEntity(siteUser,
                market);

        //mocking
        given(siteUserRepository.findById(any())).willReturn(Optional.ofNullable(siteUser));
        given(marketRepository.findById(any())).willReturn(Optional.ofNullable(market));
        given(marketPurchaseRequestRepository.save(any(MarketPurchaseRequest.class))).willReturn(
                marketPurchaseRequest);
        given(marketPurchaseRequestRepository.findById(marketPurchaseRequest.getId())).willReturn(
                Optional.ofNullable(marketPurchaseRequest));

        // when
        MarketPurchaseRequest newMarketPurchaseRequest = marketPurchaseRequestServiceImpl.createMarketPurchaseRequest(
                marketPurchaseRequestDto);

        // then
        MarketPurchaseRequest findMarketPurchaseRequest = marketPurchaseRequestRepository.findById(
                newMarketPurchaseRequest.getId()).orElse(null);

        assertEquals(marketPurchaseRequest.getId(), findMarketPurchaseRequest.getId());

    }

    @Test
    @DisplayName("시장 의뢰글 등록 실패 테스트 - USER NOT FOUND")
    void createFailedByUserNotFound() throws Exception {
        // given
        given(siteUserRepository.findById(any())).willReturn(Optional.empty());

        // when
        MarkethingException exception = assertThrows(MarkethingException.class,
                () -> marketPurchaseRequestServiceImpl.createMarketPurchaseRequest(
                        getMarketPurchaseRequestDto(getSiteUser(), getMarket())));

        // then
        assertEquals(exception.getErrorCode(), ErrorCode.USER_NOT_FOUND);
    }

    @Test
    @DisplayName("시장 의뢰글 삭제 성공 테스트")
    void deleteMarketPurchaseRequest() throws Exception {
        //given
        Market market = getMarket();
        SiteUser siteUser = getSiteUser();
        MarketPurchaseRequestDto marketPurchaseRequestDto = getMarketPurchaseRequestDto(siteUser,
                market);
        MarketPurchaseRequest marketPurchaseRequest = marketPurchaseRequestDto.toEntity(siteUser,
                market);

        // mocking
        given(marketPurchaseRequestRepository.findById(any())).willReturn(
                Optional.ofNullable(marketPurchaseRequest));

        // when
        marketPurchaseRequestServiceImpl.deleteMarketPurchaseRequest(marketPurchaseRequest.getId());

        // then
        verify(marketPurchaseRequestRepository, times(1)).delete(marketPurchaseRequest);
    }

    @Test
    @DisplayName("시장 의뢰글 삭제 실패 테스트 - USER NOT FOUND")
    void deleteFailedByRequestNotFound() throws Exception {
        // given
        given(marketPurchaseRequestRepository.findById(any())).willReturn(Optional.empty());

        // when
        MarkethingException exception = assertThrows(MarkethingException.class,
                () -> marketPurchaseRequestServiceImpl.deleteMarketPurchaseRequest(1L));

        // then
        assertEquals(exception.getErrorCode(), ErrorCode.REQUEST_NOT_FOUND);

    }

    @Test
    @DisplayName("시장 의뢰글 조회 성공 테스트")
    void getMarketPurchaseRequestSuccess() throws Exception {
        // given
        given(marketPurchaseRequestRepository.findById(1L))
                .willReturn(Optional.ofNullable(getMarketPurchaseRequest()));

        // when
        var result = marketPurchaseRequestServiceImpl.getMarketPurchaseRequest(1L);

        // then
        assertThat(result.getMarketId()).isEqualTo(getMarketPurchaseRequest().getId());
    }

    @Test
    @DisplayName("시장 의뢰글 조회 실패 테스트 - REQUEST_NOT_FOUND")
    void getMarketPurchaseRequestFailedByRequestNotFound() throws Exception {
        // given
        given(marketPurchaseRequestRepository.findById(any()))
                .willReturn(Optional.empty());

        // when
        MarkethingException exception = assertThrows(MarkethingException.class,
                () -> marketPurchaseRequestServiceImpl.getMarketPurchaseRequest(1L));

        // then
        assertEquals(exception.getErrorCode(), ErrorCode.REQUEST_NOT_FOUND);

    }

    @Test
    @DisplayName("시장 의뢰글 검색어 조회 테스트")
    void getMarketPurchaseRequestsByKeyword() throws Exception {
        // given
        KeywordDto keywordDto = new KeywordDto("content");

        PageRequest pageRequest = PageRequest.of(0, 5, Sort.unsorted());
        List<MarketPurchaseRequest> requests = new ArrayList<>();
        requests.add(getMarketPurchaseRequest());

        Page<MarketPurchaseRequest> pages
                = new PageImpl<>(requests, pageRequest, requests.size());

        given(marketPurchaseRequestRepository.findAllByFilter(keywordDto, pageRequest))
                .willReturn(pages);

        // when
        var result = marketPurchaseRequestServiceImpl.getRequestsByKeyword(keywordDto, pageRequest);

        // then
        assertThat(result.getContent().get(0).getContent()).isEqualTo(getMarketPurchaseRequest().getContent());

    }

    private static MarketPurchaseRequestPreviewDto getMarketPurchaseRequestPreviewDto() {
        return MarketPurchaseRequestPreviewDto.builder()
                .requestId(1L)
                .title("title")
                .content("content")
                .fee(15000)
                .meetupDate(LocalDate.now())
                .meetupTime(LocalTime.now())
                .marketName("marketName")
                .nickname("nickname")
                .build();
    }

    private static MarketPurchaseRequest getMarketPurchaseRequest() {
        return MarketPurchaseRequest.builder()
                .id(1L)
                .title("title")
                .content("content")
                .postImg("postImg")
                .fee(10000)
                .purchaseRequestStatus(PurchaseRequestStatus.RECRUITING)
                .meetupTime(LocalTime.now())
                .meetupDate(LocalDate.now())
                .meetupAddress("서울시")
                .market(getMarket())
                .siteUser(getSiteUser())
                .meetupLocation(geometryFactory.createPoint(
                        new Coordinate(37.56600357774501, 126.97306266269747)))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private static MarketPurchaseRequestDto getMarketPurchaseRequestDto(SiteUser siteUser,
            Market market) {
        return MarketPurchaseRequestDto.builder()
                .title("test request")
                .content("3 apples")
                .fee(15000)
                .meetupTime(LocalTime.now())
                .meetupDate(LocalDate.now())
                .meetupAddress("서울시")
                .latitude(37.5509)
                .longitude(127.0506)
                .userId(siteUser.getId())
                .marketId(market.getId())
                .build();
    }

    private static SiteUser getSiteUser() {
        return SiteUser.builder()
                .id(1L)
                .email("mockEmail@gmail.com")
                .password("password")
                .name("name")
                .nickname("nickname")
                .phoneNumber("010-1234-5678")
                .address("address")
                .myLocation(geometryFactory.createPoint(
                        new Coordinate(37.56600357774501, 126.97306266269747)))
                .mannerScore(List.of("0,0,0"))
                .profileImg("profileImg")
                .status(true)
                .authType(AuthType.GENERAL)
                .createdAt(LocalDateTime.now())
                .build();
    }

    private static Market getMarket() {
        return Market.builder()
                .id(1L)
                .idNum(04003)
                .marketName("강릉중앙시장")
                .type(1)
                .roadAddress("강원특별자치도 강릉시 금성로21")
                .streetAddress("강원특별자치도 강릉시 성남동 50")
                .location(geometryFactory.createPoint(new Coordinate(37.75402359, 128.8986233)))
                .build();
    }

}
