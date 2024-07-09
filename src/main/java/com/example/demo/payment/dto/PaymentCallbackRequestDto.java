package com.example.demo.payment.dto;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCallbackRequestDto {
    private String paymentUid; // 결제 고유 번호
    private String orderUid; // 주문 고유 번호
}
