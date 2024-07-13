package com.example.demo.marketpurchaserequest.dto;

import com.example.demo.marketpurchaserequest.entity.MarketPurchaseRequest;
import com.example.demo.type.MarketType;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;
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
    private Time meetupTime;
    private Date meetupDate;
    private String meetupAddress;
    private Double latitude;
    private Double longitude;
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
                .latitude(request.getMeetupLocation().getY())
                .longitude(request.getMeetupLocation().getX())
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
