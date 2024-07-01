package com.example.demo.entity;


import com.example.demo.type.Status;
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

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@DynamicInsert
@DynamicUpdate
@Table(name = "REPLY_COMMENT")
public class ReplyComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//자동으로 증가함.

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private SiteUser siteUser;

    @ManyToOne
    @JoinColumn(name = "COMMENT_ID", nullable = false)//자신의 부모 대댓글 만을 지정해줌
    private Comment comment;

    @Column(name = "CONTENT",length =1023,nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", length = 50, nullable = false)
    private Status status;

    @CreatedDate
    @Column(name = "CREATED_AT",nullable = false)
    private LocalDate createdAt;

    @LastModifiedDate
    @Column(name = "UPDATED_AT")
    private LocalDate updatedAt;
}
