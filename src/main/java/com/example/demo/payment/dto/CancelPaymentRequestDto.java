package com.example.demo.payment.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CancelPaymentRequestDto {
    private int amount; // 취소할 금액
    private String reason; // 취소 사유
}
