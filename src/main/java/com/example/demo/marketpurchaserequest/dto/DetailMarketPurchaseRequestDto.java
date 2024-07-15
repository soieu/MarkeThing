package com.example.demo.marketpurchaserequest.dto;

import com.example.demo.marketpurchaserequest.entity.MarketPurchaseRequest;
import com.example.demo.type.MarketType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DetailMarketPurchaseRequestDto {

    private long requestId;
    private String title;
    private String content;
    private String postImg;
    private int fee;
    private LocalTime meetupTime;
    private LocalDate meetupDate;
    private String meetupAddress;
    private double meetupLat;
    private double meetupLon;
    private long userId;
    private String nickname;
    private long marketId;
    private String marketName;
    private String marketRoadAddress;
    private String marketStreetAddress;
    private MarketType marketType;
    private LocalDateTime createdAt;

    public static DetailMarketPurchaseRequestDto fromEntity(
            MarketPurchaseRequest request) {

        return DetailMarketPurchaseRequestDto.builder()
                .requestId(request.getId())
                .title(request.getTitle())
                .content(request.getContent())
                .postImg(request.getPostImg())
                .fee(request.getFee())
                .meetupTime(request.getMeetupTime())
                .meetupDate(request.getMeetupDate())
                .meetupAddress(request.getMeetupAddress())
                .meetupLat(request.getMeetupLat())
                .meetupLon(request.getMeetupLon())
                .createdAt(request.getCreatedAt())
                .userId(request.getSiteUser().getId())
                .nickname(request.getSiteUser().getNickname())
                .marketId(request.getMarket().getId())
                .marketName(request.getMarket().getMarketName())
                .marketRoadAddress(request.getMarket().getRoadAddress())
                .marketStreetAddress(request.getMarket().getStreetAddress())
                .marketType(request.getMarket().converMarketType())
                .createdAt(request.getCreatedAt())
                .build();
    }

}
