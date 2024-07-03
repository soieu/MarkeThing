package com.example.demo.payment.repository;

import com.example.demo.payment.entity.PaymentCancelDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentCancelDetailRepository extends JpaRepository<PaymentCancelDetail, Long> {
}
