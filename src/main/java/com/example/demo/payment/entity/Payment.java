package com.example.demo.payment.entity;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.*;

import com.example.demo.entity.MarketPurchaseRequest;
import com.example.demo.type.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
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
@Table(name = "PAYMENT")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "ORDER_ID", nullable = false)
    private MarketPurchaseRequest purchaseRequest;

    @Column(name = "PAYMENT_METHOD")
    private String paymentMethod;

    @Column(name = "BANK_NAME")
    private String bankName;

    @Column(name = "CARD_NAME")
    private String cardName;

    @Column(name = "CARD_NUMBER")
    private String cardNumber;

    @Column(name = "NAME")
    private String name;

    @Column(name = "AMOUNT", nullable = false)
    private Long amount;

    @Column(name = "BUYER_NAME")
    private String buyerName;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS",nullable = false)
    private PaymentStatus status;

    @Column(name = "STARTED_AT")
    private LocalDate startedAt;

    @Column(name = "PAID_AT")
    private LocalDate paidAt;

    @Column(name = "FAILED_AT")
    private LocalDate failedAt;

    @Column(name = "FAIL_REASON")
    private String failReason;

    @Column(name = "RECEIPT_URL")
    private String receiptUrl;

    @OneToOne(mappedBy = "payment")
    private PaymentCancelDetail paymentCancelDetail;

    @CreatedDate
    @Column(name = "CREATED_AT",nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    public void changePaymentBySuccess() {
        this.status = PaymentStatus.OK;
    }
}
