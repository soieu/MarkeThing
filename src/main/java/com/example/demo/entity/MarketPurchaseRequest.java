package com.example.demo.entity;


import com.example.demo.type.AuthType;
import com.example.demo.type.PurchaseRequestStatus;
import com.example.demo.type.Status;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.locationtech.jts.geom.Point;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@DynamicInsert
@DynamicUpdate
@Table(name = "MARKET_PURCHASE_")
public class MarketPurchaseRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//자동으로 증가함.


    @Column(name = "TITLE",length =50,nullable = false)
    private String title;

    @Column(name = "CONTENT",length =1023,nullable = false)
    private String content;

    @Column(name = "POST_IMG",length =1023,nullable = false)
    private String postImg;

    @Column(name = "FEE",length =1023,nullable = false)
    private int fee;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", length = 50, nullable = false)
    private PurchaseRequestStatus status;

    @Column(name = "MEETUP_TIME",nullable = false)
    private LocalDate meetupTime;

    @Column(name = "MEETUP_DATE")
    private LocalDate meetupDate;

    @Column(name = "MEETUP_ADDRESS",nullable = false)
    private String meetupAddress;

    @OneToOne(mappedBy = "purchaseRequest")
    private RequestSuccess success; //일대일 관계를 맞음 주 테이블에 외래키가 있을때

    @OneToOne
    @JoinColumn(name = "MARKET_ID")
    private Market market;// 마켓의 아이디를 가져옴.

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private SiteUser siteUser;

    @OneToMany(mappedBy = "purchaseRequest")
    private List<ChatRoom> chatRooms;

    @OneToMany(mappedBy = "purchaseRequest")
    private List<Payment> payments;


    @Column(name = "MEETUP_LOCATION")
    private Point meetupLocation;

    @CreatedDate
    @Column(name = "CREATED_AT",nullable = false)
    private LocalDate createdAt;

    @LastModifiedDate
    @Column(name = "UPDATED_AT")
    private LocalDate updatedAt;



}
