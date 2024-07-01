package com.example.demo.entity;


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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
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
public class PaymentCancelDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//자동으로 증가함.

    @ManyToOne
    @JoinColumn(name = "PAYMENT_ID", nullable = false)
    private Payment payment;

    @Column(name = "PG_TID", nullable = false)
    private Long pgTid;

    @Column(name = "AMOUNT",nullable = false)
    private int amount;

    @Column(name = "CANCELED_AT",nullable = false)
    private String canceledAt;

    @Column(name = "REASON", nullable = false)
    private String reason;

    @Column(name = "RECEIPT_URL")
    private String receiptUrl;

}
