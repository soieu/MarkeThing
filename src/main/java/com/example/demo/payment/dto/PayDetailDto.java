package com.example.demo.payment.dto;

import com.example.demo.marketpurchaserequest.entity.MarketPurchaseRequest;
import com.example.demo.payment.entity.Pay;
import com.example.demo.siteuser.entity.SiteUser;
import com.example.demo.type.PaymentStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayDetailDto {
    private String payMethod;
    private String bankName;
    private String bankCode;
    private String cardCode;
    private String cardName;
    private String cardNumber;
    private String itemName;
    private int amount;
    private LocalDate paidAt;
    private PaymentStatus status;
    private String failReason;

    public static PayDetailDto fromEntity(Pay pay) {
        return PayDetailDto.builder()
                .payMethod(pay.getPayMethod())
                .bankName(pay.getBankName())
                .bankCode(pay.getBankCode())
                .cardCode(pay.getCardCode())
                .cardName(pay.getCardName())
                .cardNumber(pay.getCardNumber())
                .amount(pay.getAmount())
                .itemName(pay.getMarketPurchaseRequest().getTitle())
                .paidAt(pay.getPaidAt())
                .status(pay.getStatus())
                .failReason(pay.getFailReason())
                .build();
    }
}
