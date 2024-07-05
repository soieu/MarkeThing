package com.example.demo.marketpurchaserequest.service.impl;

import com.example.demo.exception.MarkethingException;
import com.example.demo.exception.type.ErrorCode;
import com.example.demo.market.entity.Market;
import com.example.demo.market.repository.MarketRepository;
import com.example.demo.marketpurchaserequest.dto.MarketPurchaseRequestDto;
import com.example.demo.marketpurchaserequest.entity.MarketPurchaseRequest;
import com.example.demo.marketpurchaserequest.repository.MarketPurchaseRequestRepository;
import com.example.demo.marketpurchaserequest.service.MarketPurchaseRequestService;
import com.example.demo.siteuser.entity.SiteUser;
import com.example.demo.siteuser.repository.SiteUserRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
        SiteUser siteUser = siteUserRepository.findById(marketPurchaseRequestDto.getUserId()).orElseThrow(() -> new MarkethingException(ErrorCode.USER_NOT_FOUND));
        Market market = marketRepository.findById(marketPurchaseRequestDto.getMarketId()).orElseThrow(()-> new MarkethingException(ErrorCode.MARKET_NOT_FOUND));
        return marketPurchaseRequestRepository.save(marketPurchaseRequestDto.toEntity(siteUser,market));
    }
}
