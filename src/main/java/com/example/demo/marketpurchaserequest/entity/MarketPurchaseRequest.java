package com.example.demo.marketpurchaserequest.entity;


import com.example.demo.chat.entiity.ChatRoom;
import com.example.demo.market.entity.Market;
import com.example.demo.entity.RequestSuccess;
import com.example.demo.payment.entity.Pay;
import com.example.demo.siteuser.entity.SiteUser;
import com.example.demo.type.PurchaseRequestStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import javax.persistence.CascadeType;
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
    private PurchaseRequestStatus purchaseRequestStatus;

    @Column(name = "MEETUP_TIME",nullable = false)
    private LocalTime meetupTime;

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
    private Pay pay;

    @Column(name = "MEETUP_LOCATION")
    private Point meetupLocation;

    @CreatedDate
    @Column(name = "CREATED_AT",nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

}