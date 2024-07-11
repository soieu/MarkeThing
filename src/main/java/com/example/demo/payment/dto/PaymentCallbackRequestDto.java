package com.example.demo.payment.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCallbackRequestDto {
    private Long paymentUid; // 결제 고유 번호
    private Long orderUid; // 주문 고유 번호
}
