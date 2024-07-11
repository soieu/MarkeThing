package com.example.demo.community.entity;


import com.example.demo.community.dto.CommunityRequestDto;
import com.example.demo.community.type.AreaType;
import com.example.demo.siteuser.entity.SiteUser;
import java.time.LocalDateTime;
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


@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
@Table(name = "COMMUNITY")
public class Community {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private SiteUser siteUser;

    @Enumerated(EnumType.STRING)
    @Column(name = "AREA", nullable = false)
    private AreaType area;

    @Column(name = "TITLE", length = 50, nullable = false)
    private String title;

    @Column(name = "CONTENT", length = 1023)
    private String content;

    @Column(name = "POST_IMG", length = 1023)
    private String postImg;

    @CreatedDate
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "UPDATED_AT")
    private LocalDateTime  updatedAt;

    public void update(CommunityRequestDto communityRequestDto) {
        area = communityRequestDto.getArea();
        title = communityRequestDto.getTitle();
        content = communityRequestDto.getContent();
        postImg = communityRequestDto.getPostImg();
    }
}
