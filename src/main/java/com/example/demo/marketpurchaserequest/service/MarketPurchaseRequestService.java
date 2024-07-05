package com.example.demo.marketpurchaserequest.service;

import com.example.demo.marketpurchaserequest.entity.MarketPurchaseRequest;

public interface MarketPurchaseRequestService {

    MarketPurchaseRequest createMarketPurchaseRequest(MarketPurchaseRequest marketPurchaseRequest, Long siteUserId, Long marketId);

}
