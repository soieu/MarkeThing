package com.example.demo.entity;


import com.example.demo.payment.entity.Payment;
import com.example.demo.siteuser.entity.SiteUser;
import com.example.demo.type.PurchaseRequestStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.*;

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

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
@Table(name = "MARKET_PURCHASE_REQUEST")
public class MarketPurchaseRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TITLE",length =50, nullable = false)
    private String title;

    @Column(name = "CONTENT",length =1023, nullable = false)
    private String content;

    @Column(name = "POST_IMG",length =1023, nullable = false)
    private String postImg;

    @Column(name = "FEE", nullable = false)
    private int fee;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", length = 50, nullable = false)
    private PurchaseRequestStatus status;

    @Column(name = "MEETUP_TIME",nullable = false)
    private LocalDateTime meetupTime;

    @Column(name = "MEETUP_DATE")
    private LocalDate meetupDate;

    @Column(name = "MEETUP_ADDRESS",nullable = false)
    private String meetupAddress;

    @OneToOne(mappedBy = "marketPurchaseRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private RequestSuccess success;

    @ManyToOne
    @JoinColumn(name = "MARKET_ID", nullable = false)
    private Market market;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private SiteUser siteUser;

    @OneToMany(mappedBy = "marketPurchaseRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatRoom> chatRooms;

    @OneToOne(mappedBy = "marketPurchaseRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private Payment payment;

    @Column(name = "MEETUP_LOCATION")
    private Point meetupLocation;

    @Column(name = "ORDER_UID")
    private String orderUid;

    @CreatedDate
    @Column(name = "CREATED_AT",nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;
}
