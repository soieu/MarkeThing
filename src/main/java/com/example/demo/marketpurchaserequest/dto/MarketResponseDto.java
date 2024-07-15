package com.example.demo.marketpurchaserequest.dto;


import com.example.demo.marketpurchaserequest.entity.MarketPurchaseRequest;
import com.example.demo.type.MarketType;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.locationtech.jts.geom.Point;

@Builder
@Getter
@AllArgsConstructor
public class MarketResponseDto {
    private long id;
    private int idNum;
    private String marketName;
    private int type;
    private double lat;
    private double lon;
    private String roadAddress; // 도로명 주소
    private String streetAddress; // 지번 주소
    public String marketType;

}
