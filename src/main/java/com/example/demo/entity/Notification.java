package com.example.demo.entity;


import com.example.demo.type.AlarmType;
import com.example.demo.type.AuthType;
import java.time.LocalDate;
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


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@DynamicInsert
@DynamicUpdate
@Table(name = "NOTIFICATION")

public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//자동으로 증가함.

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private SiteUser siteUser; //유저 아이디

    @Column(name = "IS_READ", nullable = false)
    private int isRead;

    @Column(name = "ALARM_PAGE_PATH", length = 255)
    private String pagePath;

    @Enumerated(EnumType.STRING)
    @Column(name = "ALARM_TYPE", length = 50, nullable = false)
    private AlarmType alarmType;//알람 타입을 정의함.





}
