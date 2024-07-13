package com.example.demo.payment.dto;

import com.example.demo.type.PaymentStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayResponseDto {
    private String payMethod;
    private PaymentStatus status;
    private int amount;
    private LocalDateTime createdAt;
}
