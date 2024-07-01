package com.example.demo.entity;


import com.example.demo.type.AuthType;
import com.example.demo.type.RoleType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@DynamicInsert
@DynamicUpdate
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//자동으로 증가함.

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE_TYPE", length = 50, nullable = false)
    private RoleType role;

    @ManyToMany(mappedBy = "roles")
    private List<SiteUser> siteUsers;


}
