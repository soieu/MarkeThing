package com.example.demo.marketpurchaserequest.repository;

import com.example.demo.common.filter.repository.MarketPurchaseRequestFilteringRepository;
import com.example.demo.marketpurchaserequest.entity.MarketPurchaseRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MarketPurchaseRequestRepository extends JpaRepository<MarketPurchaseRequest
        ,Long>, MarketPurchaseRequestFilteringRepository {
}
