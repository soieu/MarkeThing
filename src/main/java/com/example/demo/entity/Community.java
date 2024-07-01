package com.example.demo.entity;


import lombok.*;
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
@Table(name = "COMMUNITY")

public class Community {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//자동으로 증가함.

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private SiteUser siteUser;

    @Column(name = "AREA", nullable = false)
    private String area;

    @Column(name = "TITLE", length = 50, nullable = false)
    private String title;

    @Column(name = "CONTENT", length = 1023)
    private String content;

    @Column(name = "POST_IMG", length = 1023)
    private String postImg;

    @CreatedDate
    @Column(name = "CREATED_AT",nullable = false)
    private LocalDate createdAt;

    @LastModifiedDate
    @Column(name = "UPDATED_AT")
    private LocalDate updatedAt;





}
