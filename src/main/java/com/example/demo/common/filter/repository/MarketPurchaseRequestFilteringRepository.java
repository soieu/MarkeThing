package com.example.demo.common.filter.repository;

import com.example.demo.common.filter.dto.KeywordDto;
import com.example.demo.marketpurchaserequest.entity.MarketPurchaseRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MarketPurchaseRequestFilteringRepository {
    Page<MarketPurchaseRequest> findAllByFilter(KeywordDto keywordDto, Pageable pageable);
}
