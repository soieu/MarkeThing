package com.example.demo.marketpurchaserequest.service;

import com.example.demo.common.filter.dto.KeywordDto;
import com.example.demo.marketpurchaserequest.dto.DetailMarketPurchaseRequestDto;
import com.example.demo.marketpurchaserequest.dto.MarketPurchaseRequestDto;
import com.example.demo.marketpurchaserequest.dto.MarketPurchaseRequestPreviewDto;
import com.example.demo.marketpurchaserequest.entity.MarketPurchaseRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface MarketPurchaseRequestService {

    MarketPurchaseRequest createMarketPurchaseRequest(
            MarketPurchaseRequestDto marketPurchaseRequestDto);

    void deleteMarketPurchaseRequest(Long id);

    DetailMarketPurchaseRequestDto getMarketPurchaseRequest(Long requestId);

    Sort confirmSortOrder(String sort);

    Page<MarketPurchaseRequestPreviewDto> getRequestsByKeyword(KeywordDto keywordDto, Pageable pageable);
}
