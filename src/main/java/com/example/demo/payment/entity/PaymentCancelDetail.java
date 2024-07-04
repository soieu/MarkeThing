package com.example.demo.payment.entity;


import java.time.LocalDateTime;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
@Table(name = "PAYMENT_CANCEL_DETAIL")
public class PaymentCancelDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "PAYMENT_ID", nullable = false)
    private Payment payment;

    @Column(name = "PG_TID", nullable = false)
    private Long pgTid;

    @Column(name = "AMOUNT", nullable = false)
    private int amount;

    @Column(name = "CANCELED_AT", nullable = false)
    private LocalDateTime canceledAt;

    @Column(name = "REASON", nullable = false)
    private String reason;

    @Column(name = "RECEIPT_URL")
    private String receiptUrl;
}
