package com.example.demo.marketpurchaserequest.dto;

import com.example.demo.market.entity.Market;
import com.example.demo.marketpurchaserequest.entity.MarketPurchaseRequest;
import com.example.demo.siteuser.entity.SiteUser;
import java.sql.Time;
import java.time.LocalDateTime;
import java.sql.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

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
    private Time meetupTime;

    @NotEmpty(message = "약속 날짜 입력하세요.")
    private Date meetupDate;

    @NotBlank(message = "약속 주소를 입력하세요.")
    private String meetupAddress;

    private Double latitude;
    private Double longitude;

    private Long userId;

    private Long marketId;

    @Builder
    public MarketPurchaseRequestDto(String title, String content, String postImg, int fee,
            Time meetupTime, Date meetupDate, String meetupAddress, double latitude, double longitude, Long userId, Long marketId) {
        this.title = title;
        this.content = content;
        this.postImg = postImg;
        this.fee = fee;
        this.meetupTime = meetupTime;
        this.meetupDate = meetupDate;
        this.meetupAddress = meetupAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.userId = userId;
        this.marketId = marketId;
    }

    public Point getPoint(double latitude, double longitude) {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        return geometryFactory.createPoint(new Coordinate(latitude, longitude));
    }

    public MarketPurchaseRequest toEntity(SiteUser siteUser, Market market) {
        return MarketPurchaseRequest.builder()
                .title(title)
                .content(content)
                .postImg(postImg)
                .fee(fee)
                .meetupTime(meetupTime)
                .meetupDate(meetupDate)
                .meetupAddress(meetupAddress)
                .meetupLocation(getPoint(longitude, longitude))
                .createdAt(LocalDateTime.now())
                .siteUser(siteUser)
                .market(market)
                .build();

    }

}
