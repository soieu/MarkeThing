package com.example.demo.payment.repository;

import com.example.demo.payment.dto.PayResponseDto;
import com.example.demo.payment.entity.Pay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Pay, Long> {

    @Query("SELECT new com.example.demo.payment.dto.PayResponseDto(p.payMethod, p.status, p.amount, p.createdAt) " +
            "FROM Pay p " +
            "WHERE p.id = :id " +
            "ORDER BY p.createdAt")
    List<PayResponseDto> findPayResponseDtoById(@Param("id") Long id);

}
