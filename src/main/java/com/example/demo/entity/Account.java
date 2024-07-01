package com.example.demo.entity;


import com.example.demo.type.Status;
import java.time.LocalDate;
import java.util.List;
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
@DynamicInsert//불필요한 컬럼을 제외하고 실제 변경된 데이터만 포함하는 SQL을 생성을 합니다.
@DynamicUpdate
@Table(name = "ACCOUNT")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//자동으로 증가함.

    @Column(name = "BANK",length =50,nullable = false)
    private String bank;

    @Column(name = "ACCOUNT_NUMBER",length =50,nullable = false)
    private String account;

    @Column(name = "HOLDER",length =50,nullable = false)
    private String holder;

    @OneToOne
    @JoinColumn(name = "USER_ID")
    private SiteUser siteUser;



}
