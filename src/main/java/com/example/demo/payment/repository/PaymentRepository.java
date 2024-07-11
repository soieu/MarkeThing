package com.example.demo.payment.repository;

import com.example.demo.payment.entity.Pay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Pay, Long> {
}
