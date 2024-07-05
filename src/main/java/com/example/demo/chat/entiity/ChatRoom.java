package com.example.demo.chat.entiity;


import com.example.demo.marketpurchaserequest.entity.MarketPurchaseRequest;
import com.example.demo.siteuser.entity.SiteUser;
import java.time.LocalDateTime;
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
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
@Table(name = "CHATROOM")
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "REQUEST_ID", nullable = false) // 구매 대행 글
    private MarketPurchaseRequest purchaseRequest;

    @OneToOne
    @JoinColumn(name = "REQUESTER_ID", nullable = false) // 수신자 아이디
    private SiteUser requester;

    @OneToOne
    @JoinColumn(name = "AGENT_ID", nullable = false) // 발신자 아이디
    private SiteUser agent;

//    @OneToMany(mappedBy = "chatRoom") //채팅 메시지를 mongoDB에서 저장을 하기 때문에 의존관계 제거
//    private List<ChatMessage> chatMessages;

    @CreatedDate
    @Column(name = "CREATED_AT",nullable = false)
    private LocalDateTime createdAt;
}
