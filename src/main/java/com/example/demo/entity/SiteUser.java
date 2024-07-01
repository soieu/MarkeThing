package com.example.demo.entity;

import com.example.demo.auth.dto.SignupDto;
import com.example.demo.type.AuthType;
import java.util.ArrayList;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name="SITE_USER")
@Table(name= "SITE_USER")

public class SiteUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "EMAIL", unique = true, nullable = false)
    private String email;

    @Column(name = "PASSWORD", length = 50, nullable = false)
    private String password;

    @Column(name = "NAME", length = 50, nullable = false)
    private String name;

    @Column(name = "NICKNAME", length = 50, nullable = false)
    private String nickname;

    @Column(name = "PHONE_NUMBER", length = 50, nullable = false)
    private String phoneNumber;

    @Column(name = "ADDRESS", length = 50, nullable = false)
    private String address;

    @Column(name = "MANNER_SCORE")
    private Integer mannerScore;

    @Column(name = "PROFILE_IMG", length =1023, nullable = false)
    private String profileImg;

    @Column(name = "STATUS", nullable = false)
    private boolean status;

    @Enumerated(EnumType.STRING)
    @Column(name = "AUTH_TYPE", length = 50, nullable = false)
    private AuthType authType; //회원의 가입 상태.

    @OneToMany(mappedBy = "siteUser")
    private List<Community> communities;

    @OneToMany(mappedBy = "siteUser")
    private List<Comment> comments;

    @OneToMany(mappedBy = "siteUser")
    private List<ReplyComment> replyComments;

    @OneToMany(mappedBy = "siteUser")
    private List<Bookmark> bookmarks;


    @OneToMany(mappedBy = "agent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Manner> agents;

    // 평가를 한 목록
    @OneToMany(mappedBy = "requester", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Manner> requesters;

    @OneToMany(mappedBy = "siteUser")
    private List<MarketPurchaseRequest> purchaseRequests;

    @OneToMany(mappedBy = "siteUser")
    private List<ChatMessage> chatMessages;

    @OneToOne(mappedBy = "siteUser")
    private RequestSuccess success; //일대일 관계를 맞음 주 테이블에 외래키가 있을때

    @OneToOne(mappedBy = "siteUser")
    private Account account;

    @OneToOne(mappedBy = "siteUser")
    private RequestSuccess requestSuccess;

//    @OneToMany(mappedBy = "siteUser")
//    private List<Manner> agents;//피평가자.

    //한테이블이 한 테이블의 연관 관계를 두개의 속성이르 이러쿵 저러쿵..이거 해결을 해야 겠다.

    @OneToOne(mappedBy = "requester")
    private ChatRoom requester;

    @OneToOne(mappedBy = "agent")
    private ChatRoom agent;

    @ManyToMany
    @JoinTable(name = "USER_ROLE", joinColumns = @JoinColumn(name = "USER_ID"),
                inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
    private List<Role> roles = new ArrayList<>();

    @CreatedDate
    @Column(name = "CREATED_AT",nullable = false)
    private LocalDate createdAt;

    @LastModifiedDate
    @Column(name = "UPDATED_AT")
    private LocalDate updatedAt;


    public static SiteUser fromDto(SignupDto signupDto){
        return SiteUser.builder()
                .email(signupDto.getEmail())
                .password(signupDto.getPassword())
                .name(signupDto.getName())
                .nickname(signupDto.getNickname())
                .phoneNumber(signupDto.getPhoneNumber())
                .address(signupDto.getAddress())
                .authType(AuthType.GENERAL)
                .status(true)//디폴트로는 그냥 이용 가능으로.  해줌.
                .build(); //user 테이블 creat
    }








}
