package com.example.demo.entity;


import com.example.demo.community.entity.Community;
import com.example.demo.siteuser.entity.SiteUser;
import com.example.demo.type.PostStatus;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
@Table(name = "COMMENT")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private SiteUser siteUser;

    @ManyToOne
    @JoinColumn(name = "POST_ID", nullable = false)
    private Community community;

    @Column(name = "CONTENT",length =1023, nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", length = 50, nullable = false)
    private PostStatus postStatus;

    @OneToMany(mappedBy = "comment")
    private List<ReplyComment> replyComments;

    @CreatedDate
    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;
}
