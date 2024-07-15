package com.example.demo.marketpurchaserequest.service.impl;

import static com.example.demo.exception.type.ErrorCode.KAKAO_LOCAL_ERROR;

import com.example.demo.common.filter.dto.marketpurchaserequest.KeywordDto;
import com.example.demo.common.filter.dto.marketpurchaserequest.MarketFilterDto;
import com.example.demo.common.filter.dto.marketpurchaserequest.MarketPurchaseRequestFilterDto;
import com.example.demo.common.kakao.KakaoLocalService;
import com.example.demo.exception.MarkethingException;
import com.example.demo.exception.type.ErrorCode;
import com.example.demo.marketpurchaserequest.dto.MarketResponseDto;
import com.example.demo.marketpurchaserequest.entity.Market;
import com.example.demo.marketpurchaserequest.repository.MarketRepository;
import com.example.demo.marketpurchaserequest.dto.DetailMarketPurchaseRequestDto;
import com.example.demo.marketpurchaserequest.dto.MarketPurchaseRequestDto;
import com.example.demo.marketpurchaserequest.dto.MarketPurchaseRequestPreviewDto;
import com.example.demo.marketpurchaserequest.entity.MarketPurchaseRequest;
import com.example.demo.marketpurchaserequest.repository.MarketPurchaseRequestRepository;
import com.example.demo.marketpurchaserequest.service.MarketPurchaseRequestService;
import com.example.demo.siteuser.entity.SiteUser;
import com.example.demo.siteuser.repository.SiteUserRepository;
import com.example.demo.util.PointUtils;
import java.net.URISyntaxException;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MarketPurchaseRequestServiceImpl implements MarketPurchaseRequestService {

    private final MarketPurchaseRequestRepository marketPurchaseRequestRepository;
    private final SiteUserRepository siteUserRepository;
    private final MarketRepository marketRepository;
    private final KakaoLocalService kakaoLocalService;

    @Override
    @Transactional
    public MarketPurchaseRequest createMarketPurchaseRequest(
            MarketPurchaseRequestDto marketPurchaseRequestDto) {
        SiteUser siteUser = siteUserRepository.findById(marketPurchaseRequestDto.getUserId())
                .orElseThrow(() -> new MarkethingException(ErrorCode.USER_NOT_FOUND));

        Market market = marketRepository.findById(marketPurchaseRequestDto.getMarketId())
                .orElseThrow(()-> new MarkethingException(ErrorCode.MARKET_NOT_FOUND));

        try {
            return marketPurchaseRequestRepository.save(marketPurchaseRequestDto
                    .toEntity(siteUser, market, kakaoLocalService.getAddress(
                            marketPurchaseRequestDto.getMeetupLat(),
                            marketPurchaseRequestDto.getMeetupLon())));
        } catch (URISyntaxException e) {
            throw new MarkethingException(KAKAO_LOCAL_ERROR);
        }
    }

    @Override
    public void deleteMarketPurchaseRequest(Long id) {
        MarketPurchaseRequest marketPurchaseRequest = marketPurchaseRequestRepository.findById(id)
                .orElseThrow(() -> new MarkethingException(ErrorCode.REQUEST_NOT_FOUND));
        marketPurchaseRequestRepository.delete(marketPurchaseRequest);
    }

    @Override
    public DetailMarketPurchaseRequestDto getMarketPurchaseRequest(Long requestId) {
        return DetailMarketPurchaseRequestDto
                .fromEntity(
                        marketPurchaseRequestRepository.findById(requestId)
                .orElseThrow(() -> new MarkethingException(ErrorCode.REQUEST_NOT_FOUND))
                );
    }

    @Override
    public Sort confirmSortOrder(String sort) {
        Sort sortOrder = Sort.unsorted();
        if("register".equals(sort)) {
            sortOrder = Sort.by("createdAt").descending(); // 최신순으로 정렬
            return sortOrder;
        }
        if("meetup".equals(sort)) {
            sortOrder = Sort.by(
                    Order.asc("meetupDate"), // MeetupDate 기준으로 오름차순 정렬
                    Order.asc("meetupTime"));  // MeetupTime 기준으로 오름차순 정렬
            return sortOrder;
        }
        if("fee".equals(sort)) {
            sortOrder = Sort.by("fee").descending(); // 수수료 높은순 정렬
            return sortOrder;
        }
        return sortOrder;
    }

    @Override
    public Sort confirmMarketSortOrder(String sort) {
        return Sort.by("marketName").ascending();
    }

    @Override
    public Page<MarketResponseDto> getMarketsByFilter(MarketFilterDto marketFilterDto,
            Pageable pageable) {
        return marketRepository.findAllByFilter(marketFilterDto, pageable)
                .map(MarketResponseDto::fromEntity);
    }

    @Override
    public Page<MarketPurchaseRequestPreviewDto> getRequestsByKeyword(KeywordDto keywordDto,
            Pageable pageable) {
        if (keywordDto.getKeyword().isBlank()) {
            return marketPurchaseRequestRepository.findAll(pageable)
                    .map(MarketPurchaseRequestPreviewDto::fromEntity);
        }
        return marketPurchaseRequestRepository.findAllByFilter(keywordDto, pageable)
                .map(MarketPurchaseRequestPreviewDto::fromEntity);
     }

    @Override
    public Page<MarketPurchaseRequestPreviewDto> getRequestsWithinDistance(String email,
            double distance, Pageable pageable) {
        Point myLocation = siteUserRepository.findByEmail(email)
                .orElseThrow(() -> new MarkethingException(ErrorCode.USER_NOT_FOUND))
                .getMyLocation();

        Point northEastBound = PointUtils.getBoudaryPoint(myLocation, distance, 45.0);
        Point southWestBound = PointUtils.getBoudaryPoint(myLocation, distance, 225.0);

        return marketPurchaseRequestRepository
                .findAllWithinBoundary(myLocation, northEastBound, southWestBound, pageable)
                .map(MarketPurchaseRequestPreviewDto::fromEntity);
    }

    @Override
    public Page<MarketPurchaseRequestPreviewDto> getRequestsByFilter(
            MarketPurchaseRequestFilterDto filterDto, Pageable pageable) {
        if (filterDto.isEmpty()) {
            return marketPurchaseRequestRepository.findAll(pageable)
                    .map(MarketPurchaseRequestPreviewDto::fromEntity);
        }
        return marketPurchaseRequestRepository.findAllByFilter(filterDto, pageable)
                .map(MarketPurchaseRequestPreviewDto::fromEntity);
    }
}
