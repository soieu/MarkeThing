package com.example.demo.payment.entity;


import com.example.demo.marketpurchaserequest.entity.MarketPurchaseRequest;
import com.example.demo.siteuser.entity.SiteUser;
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

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
@Table(name = "PAY")
public class Pay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private SiteUser siteUser;

    @OneToOne
    @JoinColumn(name = "ORDER_ID", nullable = false)
    private MarketPurchaseRequest marketPurchaseRequest;

    @Column(name = "IMP_UID", nullable = false)
    private String impUid;

    @Column(name = "PAY_METHOD")
    private String payMethod;

    @Column(name = "APPLY_NUM")
    private String applyNum;

    @Column(name = "BANK_CODE")
    private String bankCode;

    @Column(name = "BANK_NAME")
    private String bankName;

    @Column(name = "CARD_CODE", nullable = false)
    private String cardCode;

    @Column(name = "CARD_NAME")
    private String cardName;

    @Column(name = "CARD_NUMBER")
    private String cardNumber;

    @Column(name = "CARD_QUOTE")
    private Long cardQuote;

    @Column(name = "NAME")
    private String name;

    @Column(name = "AMOUNT", nullable = false)
    private int amount;

    @Column(name = "BUYER_NAME")
    private String buyerName;

    @Column(name = "BUYER_EMAIL")
    private String buyerEmail;

    @Column(name = "BUYER_TEL")
    private String buyerTel;

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

    @Column(name = "CASH_RECEIPT_ISSUED")
    private String cashReceiptIssue;

    @Column(name = "BUYER_ADDR",nullable = false)
    private Long buyerAddr;

    @OneToOne(mappedBy = "pay", cascade = CascadeType.ALL, orphanRemoval = true)
    private PaymentCancelDetail paymentCancelDetails;

    @CreatedDate
    @Column(name = "CREATED_AT",nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    public void changePaymentBySuccess() {
            status = PaymentStatus.OK;
    }
    public void changePaymentByCancel() {
        status = PaymentStatus.CANCEL;
    }

}
