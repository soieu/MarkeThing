package com.example.demo.marketpurchaserequest.repository;

import com.example.demo.common.filter.repository.request.MarketFilteringRepository;
import com.example.demo.marketpurchaserequest.entity.Market;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarketRepository extends JpaRepository<Market,Long>, MarketFilteringRepository {

}
