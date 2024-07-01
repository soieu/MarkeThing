package com.example.demo.entity;


import java.time.LocalDate;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
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
@Table(name = "POST")

public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//자동으로 증가함.

    @ManyToOne
    @JoinColumn(name = "REQUEST_ID", nullable = false)
    private MarketPurchaseRequest purchaseRequest;

    @OneToOne
    @JoinColumn(name = "REQUESTER_ID")
    private SiteUser requester;

    @OneToOne
    @JoinColumn(name = "AGENT_ID")
    private SiteUser agent;

    @OneToMany(mappedBy = "chatRoom")
    private List<ChatMessage> chatMessages;

    @CreatedDate
    @Column(name = "CREATED_AT",nullable = false)
    private LocalDate createdAt;


}
