package com.example.demo.marketpurchaserequest.dto;

import com.example.demo.marketpurchaserequest.entity.MarketPurchaseRequest;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MarketPurchaseRequestPreviewDto {

    private long requestId;
    private String title;
    private String content;
    private int fee;
    private Time meetupTime;
    private Date meetupDate;
    private String meetupAddress;
    private String nickname;
    private String marketName;
    private LocalDateTime createdAt;

    public static MarketPurchaseRequestPreviewDto fromEntity(
            MarketPurchaseRequest request) {

        return MarketPurchaseRequestPreviewDto.builder()
                .requestId(request.getId())
                .title(request.getTitle())
                .content(request.getContent())
                .fee(request.getFee())
                .meetupTime(request.getMeetupTime())
                .meetupDate(request.getMeetupDate())
                .meetupAddress(request.getMeetupAddress())
                .nickname(request.getSiteUser().getNickname())
                .marketName(request.getMarket().getMarketName())
                .createdAt(request.getCreatedAt())
                .build();
    }
}