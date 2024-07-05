package com.example.demo.payment.repository;

import com.example.demo.entity.MarketPurchaseRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MarketPurchaseRequestRepository extends JpaRepository<MarketPurchaseRequest, Long> {
    @Query("select m from MarketPurchaseRequest m" +
            " left join fetch m.payment p" +
            " left join fetch m.siteUser s" +
            " where m.orderUid = :orderUid")
    Optional<MarketPurchaseRequest> findOrderAndPaymentAndMember(String orderUid);

    @Query("select m from MarketPurchaseRequest m" +
            " left join fetch m.payment p" +
            " where m.orderUid = :orderUid")
    Optional<MarketPurchaseRequest> findOrderAndPayment(String orderUid);

}
