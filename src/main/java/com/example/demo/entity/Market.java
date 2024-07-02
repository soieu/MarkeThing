package com.example.demo.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
    private Long id;//자동으로 증가함.

    @OneToOne(mappedBy = "market")
    private MarketPurchaseRequest  purchaseRequest;

    @Column(name = "ID_NUM",nullable = false)
    private int idNum;

    @Column(name = "MARKET_NAME", nullable = false)
    private String marketName;

    @Column(name = "MARKET_TYPE",nullable = false)
    private int type;

    @Column(name = "LOCATION", nullable = false)
    private Point point;

    @Column(name = "ROAD_ADDRESS", nullable = false)
    private String roadAddress; // 도로명 주소

    @Column(name = "STREET_ADDRESS", nullable = false)
    private int streetAddress; // 지번 주소
}
