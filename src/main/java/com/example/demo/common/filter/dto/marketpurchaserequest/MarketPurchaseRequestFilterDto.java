package com.example.demo.common.filter.dto.marketpurchaserequest;

import com.example.demo.type.PurchaseRequestStatus;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MarketPurchaseRequestFilterDto {
    private PurchaseRequestStatus purchaseRequestStatus;
    private LocalDate meetupStartDt;
    private LocalDate meetupEndDt;

    public boolean isEmpty() {
        return purchaseRequestStatus == null
                && meetupEndDt == null
                && meetupStartDt == null;
    }
}