package com.example.demo.siteuser.entity;


import com.example.demo.type.Rate;
import java.time.LocalDateTime;
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

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
@Table(name = "MANNER")
public class Manner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TAKER_ID")
    private SiteUser taker;

    // 평가를 하는 사용자와의 다대일 관계를 정의
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RATER_ID")
    private SiteUser rater;

    @Enumerated(EnumType.STRING)
    @Column(name = "RATE", length = 50, nullable = false)
    private Rate rate;

    @CreatedDate
    @Column(name = "CREATED_AT",nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;
}
