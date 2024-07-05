package com.example.demo.marketpurchaserequest.service;

import com.example.demo.marketpurchaserequest.dto.MarketPurchaseRequestDto;
import com.example.demo.marketpurchaserequest.entity.MarketPurchaseRequest;

public interface MarketPurchaseRequestService {

    MarketPurchaseRequest createMarketPurchaseRequest(
            MarketPurchaseRequestDto marketPurchaseRequestDto);
}
