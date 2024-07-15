package com.example.demo.market.entity;


import com.example.demo.marketpurchaserequest.entity.MarketPurchaseRequest;
import com.example.demo.type.MarketType;
import java.util.List;
import javax.persistence.CascadeType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
@Table(name = "MARKET")
public class Market {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ID_NUM",nullable = false)
    private int idNum;

    @Column(name = "MARKET_NAME", nullable = false)
    private String marketName;

    @Column(name = "MARKET_TYPE",nullable = false)
    private int type;

    @Column(name = "LAT", nullable = false)
    private Double lat;

    @Column(name = "LOT", nullable = false)
    private Double lot;

    @Column(name = "ROAD_ADDRESS", nullable = false)
    private String roadAddress; // 도로명 주소

    @Column(name = "STREET_ADDRESS", nullable = false)
    private String streetAddress; // 지번 주소

    @OneToMany(mappedBy = "market", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MarketPurchaseRequest> marketPurchaseRequests;

    public MarketType converMarketType() {
        if(type == MarketType.FIVE_DAY.getValue()) {
            return MarketType.FIVE_DAY;
        }
        return MarketType.REGULAR;
    }
}
