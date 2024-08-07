package com.example.demo.chat.entiity;


import com.example.demo.marketpurchaserequest.entity.MarketPurchaseRequest;
import com.example.demo.siteuser.entity.SiteUser;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
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
import org.hibernate.annotations.ColumnDefault;
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

    @Column(name = "CHAT_ROOM_STATUS",nullable = false)
    @ColumnDefault("2")
    private int chatRoomStatus;

    @ManyToOne
    @JoinColumn(name = "REQUEST_ID", nullable = false)
    private MarketPurchaseRequest marketPurchaseRequest;

    @ManyToOne
    @JoinColumn(name = "REQUESTER_ID")
    private SiteUser requester;

    @ManyToOne
    @JoinColumn(name = "AGENT_ID")
    private SiteUser agent;

    @CreatedDate
    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    public void detachRequester() {
        this.requester = null;
    }

    public void detachAgent() {
        this.agent = null;
    }
    public  void minusStatus() {
        this.chatRoomStatus-=1;
    }
}
