package com.example.demo.entity;


import com.example.demo.siteuser.entity.SiteUser;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "CHAT_MESSAGE")

public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ROOM_ID", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "SENDER_ID", nullable = false)
    private SiteUser siteUser;

    @Column(name = "CONTENT", length = 1023, nullable = false)
    private String content;

    @CreatedDate
    @Column(name = "CREATED_AT",nullable = false)
    private LocalDate createdAt;
}
