package com.example.demo.payment.dto;

import com.example.demo.siteuser.entity.SiteUser;
import com.example.demo.type.PaymentStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
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


}
