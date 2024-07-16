package com.example.demo.marketpurchaserequest.dto;

import com.example.demo.marketpurchaserequest.entity.Market;
import com.example.demo.marketpurchaserequest.entity.MarketPurchaseRequest;
import com.example.demo.siteuser.entity.SiteUser;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MarketPurchaseRequestDto {

    @NotBlank(message = "제목을 입력하세요.")
    private String title;

    @NotBlank(message = "내용을 입력하세요.")
    private String content;

    private String postImg;

    @NotEmpty(message = "수수료를 입력하세요.")
    private int fee;

    @NotEmpty(message = "약속 시간을 입력하세요.")
    private LocalTime meetupTime;

    @NotEmpty(message = "약속 날짜 입력하세요.")
    private LocalDate meetupDate;

    private Double meetupLat;
    private Double meetupLon;

    private Long marketId;

    public MarketPurchaseRequest toEntity(SiteUser siteUser, Market market, String meetupAddress) {
        return MarketPurchaseRequest.builder()
                .title(title)
                .content(content)
                .postImg(postImg)
                .fee(fee)
                .meetupAddress(meetupAddress)
                .meetupTime(meetupTime)
                .meetupDate(meetupDate)
                .meetupLat(meetupLat)
                .meetupLon(meetupLon)
                .createdAt(LocalDateTime.now())
                .siteUser(siteUser)
                .market(market)
                .build();
    }
}