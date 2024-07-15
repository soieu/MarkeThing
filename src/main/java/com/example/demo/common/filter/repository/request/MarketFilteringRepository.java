package com.example.demo.common.filter.repository.request;

import com.example.demo.common.filter.dto.marketpurchaserequest.MarketFilterDto;
import com.example.demo.marketpurchaserequest.entity.MarketPurchaseRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MarketFilteringRepository {
    Page<MarketPurchaseRequest> findAllByFilter(MarketFilterDto marketFilterDto, Pageable pageable);
}
