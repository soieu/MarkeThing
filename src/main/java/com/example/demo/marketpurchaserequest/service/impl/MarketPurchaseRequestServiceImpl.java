package com.example.demo.marketpurchaserequest.service.impl;

import com.example.demo.common.filter.dto.KeywordDto;
import com.example.demo.exception.MarkethingException;
import com.example.demo.exception.type.ErrorCode;
import com.example.demo.market.entity.Market;
import com.example.demo.market.repository.MarketRepository;
import com.example.demo.marketpurchaserequest.dto.DetailMarketPurchaseRequestDto;
import com.example.demo.marketpurchaserequest.dto.MarketPurchaseRequestDto;
import com.example.demo.marketpurchaserequest.dto.MarketPurchaseRequestPreviewDto;
import com.example.demo.marketpurchaserequest.entity.MarketPurchaseRequest;
import com.example.demo.marketpurchaserequest.repository.MarketPurchaseRequestRepository;
import com.example.demo.marketpurchaserequest.service.MarketPurchaseRequestService;
import com.example.demo.siteuser.entity.SiteUser;
import com.example.demo.siteuser.repository.SiteUserRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

    @Override
    @Transactional
    public MarketPurchaseRequest createMarketPurchaseRequest(
            MarketPurchaseRequestDto marketPurchaseRequestDto) {
        SiteUser siteUser = siteUserRepository.findById(marketPurchaseRequestDto.getUserId())
                .orElseThrow(() -> new MarkethingException(ErrorCode.USER_NOT_FOUND));

        Market market = marketRepository.findById(marketPurchaseRequestDto.getMarketId())
                .orElseThrow(()-> new MarkethingException(ErrorCode.MARKET_NOT_FOUND));

        return marketPurchaseRequestRepository.save(marketPurchaseRequestDto
                .toEntity(siteUser,market));
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
            sortOrder = Sort.by("createdAt").descending();
            return sortOrder;
        }
        if("meetup".equals(sort)) {
            sortOrder = Sort.by(
                    Order.desc("meetupDate"), // MeetupDate를 내림차순으로 정렬
                    Order.desc("meetupTime"));  // MeetupTime을 내림차순으로 정렬
            return sortOrder;
        }
        return sortOrder;
    }

    @Override
    public Page<MarketPurchaseRequestPreviewDto> getRequestByKeyword(KeywordDto keywordDto,
            Pageable pageable) {
        if (keywordDto.getKeyword().isBlank()) {
            return marketPurchaseRequestRepository.findAll(pageable)
                    .map(MarketPurchaseRequestPreviewDto::fromEntity);
        }
        return marketPurchaseRequestRepository.findAllByFilter(keywordDto, pageable)
                .map(MarketPurchaseRequestPreviewDto::fromEntity);
     }
}
