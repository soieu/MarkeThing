package com.example.demo.marketpurchaserequest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.example.demo.exception.MarkethingException;
import com.example.demo.exception.type.ErrorCode;
import com.example.demo.market.entity.Market;
import com.example.demo.market.repository.MarketRepository;
import com.example.demo.marketpurchaserequest.dto.MarketPurchaseRequestDto;
import com.example.demo.marketpurchaserequest.entity.MarketPurchaseRequest;
import com.example.demo.marketpurchaserequest.repository.MarketPurchaseRequestRepository;
import com.example.demo.marketpurchaserequest.service.impl.MarketPurchaseRequestServiceImpl;
import com.example.demo.siteuser.entity.SiteUser;
import com.example.demo.siteuser.repository.SiteUserRepository;
import com.example.demo.type.AuthType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    void createMarketPurchaseRequest() {
        //given
        Market market = getMarket();
        SiteUser siteUser = getSiteUser();
        MarketPurchaseRequestDto marketPurchaseRequestDto = getMarketPurchaseRequestDto(siteUser,market);
        MarketPurchaseRequest marketPurchaseRequest = marketPurchaseRequestDto.toEntity(siteUser, market);

        //mocking
        given(siteUserRepository.findById(any())).willReturn(Optional.ofNullable(siteUser));
        given(marketRepository.findById(any())).willReturn(Optional.ofNullable(market));
        given(marketPurchaseRequestRepository.save(any(MarketPurchaseRequest.class))).willReturn(marketPurchaseRequest);
        given(marketPurchaseRequestRepository.findById(marketPurchaseRequest.getId())).willReturn(Optional.ofNullable(marketPurchaseRequest));

        // when
        MarketPurchaseRequest newMarketPurchaseRequest = marketPurchaseRequestServiceImpl.createMarketPurchaseRequest(marketPurchaseRequestDto);

        // then
        MarketPurchaseRequest findMarketPurchaseRequest = marketPurchaseRequestRepository.findById(newMarketPurchaseRequest.getId()).orElse(null);

        assertEquals(marketPurchaseRequest.getId(),findMarketPurchaseRequest.getId());

    }

    @Test
    void createFailedByUserNotFound(){
        // given
        given(siteUserRepository.findById(any())).willReturn(Optional.empty());

        // when
        MarkethingException exception = assertThrows(MarkethingException.class,
                () -> marketPurchaseRequestServiceImpl.createMarketPurchaseRequest(getMarketPurchaseRequestDto(getSiteUser(), getMarket())));

        // then
        assertEquals(exception.getErrorCode(), ErrorCode.USER_NOT_FOUND);

    }

    private static MarketPurchaseRequestDto getMarketPurchaseRequestDto(SiteUser siteUser, Market market) {
        return MarketPurchaseRequestDto.builder()
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
                .myLocation(geometryFactory.createPoint(new Coordinate(37.56600357774501, 126.97306266269747)))
                .mannerScore(0)
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
