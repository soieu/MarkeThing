package com.example.demo.payment.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class RequestPayDto {
    private String orderUid;
    private String itemName;
    private String buyerName;
    private Long paymentPrice;
}
