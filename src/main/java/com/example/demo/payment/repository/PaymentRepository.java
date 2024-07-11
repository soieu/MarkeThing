package com.example.demo.payment.repository;

import com.example.demo.payment.entity.Pay;
import com.example.demo.siteuser.entity.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Pay, Long> {
    List<Pay> findBySiteUser(Optional<SiteUser> siteUser);
}
