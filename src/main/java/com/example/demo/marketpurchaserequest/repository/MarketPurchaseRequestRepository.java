package com.example.demo.marketpurchaserequest.repository;

import com.example.demo.marketpurchaserequest.entity.MarketPurchaseRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MarketPurchaseRequestRepository extends JpaRepository<MarketPurchaseRequest,
        Long> {
    @Query("select m from MarketPurchaseRequest m" +
            " left join fetch m.payment p" +
            " left join fetch m.siteUser s" +
            " where m.id = :id")
    Optional<MarketPurchaseRequest> findMarketPurchaseRequestAndPaymentAndSiteUser(String id);

    @Query("select m from MarketPurchaseRequest m" +
            " left join fetch m.payment p" +
            " where m.id = :id")
    Optional<MarketPurchaseRequest> findMarketPurchaseRequestAndPayment(String id);

}
