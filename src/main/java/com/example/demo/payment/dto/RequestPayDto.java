package com.example.demo.payment.dto;

import com.example.demo.payment.entity.Pay;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestPayDto {
    private Long orderUid;
    private String itemName;
    private String buyerName;
    private int paymentPrice;

    public static RequestPayDto fromEntity(Pay pay) {
        return RequestPayDto
                .builder()
                .paymentPrice(pay.getAmount())
                .orderUid(pay.getMarketPurchaseRequest() != null ? pay.getMarketPurchaseRequest().getId() : null)
                .buyerName(pay.getBuyerName())
                .itemName(pay.getMarketPurchaseRequest() != null ? pay.getMarketPurchaseRequest().getTitle() : "")
                .build();
    }

}

