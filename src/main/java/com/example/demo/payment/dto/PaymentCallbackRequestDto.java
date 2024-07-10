package com.example.demo.payment.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PaymentCallbackRequestDto {
    private Long paymentUid; // 결제 고유 번호
    private Long orderUid; // 주문 고유 번호
}
