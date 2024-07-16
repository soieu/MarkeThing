package com.example.demo.marketpurchaserequest.dto;


import com.example.demo.marketpurchaserequest.entity.Market;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class MarketResponseDto {
    private String idNum;
    private long id;
    private String marketName;
    private double lat;
    private double lon;
    private String roadAddress; // 도로명 주소
    private String streetAddress; // 지번 주소
    public String marketType;

    public static MarketResponseDto fromEntity(Market market) {
        return MarketResponseDto.builder()
                .idNum(market.getIdNum())
                .id(market.getId())
                .marketName(market.getMarketName())
                .marketType(market.converMarketType().getName())
                .lat(market.getLat())
                .lon(market.getLon())
                .roadAddress(market.getRoadAddress())
                .streetAddress(market.getStreetAddress())
                .build();
    }
}
