package com.example.demo.common.filter.repository.request;

import com.example.demo.common.filter.dto.marketpurchaserequest.KeywordDto;
import com.example.demo.common.filter.dto.marketpurchaserequest.MarketPurchaseRequestFilterDto;
import com.example.demo.marketpurchaserequest.entity.MarketPurchaseRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MarketPurchaseRequestFilteringRepository {
    Page<MarketPurchaseRequest> findAllByFilter(KeywordDto keywordDto, Pageable pageable);
    Page<MarketPurchaseRequest> findAllByFilter(MarketPurchaseRequestFilterDto filterDto,
            Pageable pageable);
}
