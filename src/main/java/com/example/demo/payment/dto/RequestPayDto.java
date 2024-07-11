package com.example.demo.payment.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestPayDto {
    private Long orderUid;
    private String itemName;
    private String buyerName;
    private int paymentPrice;
}
