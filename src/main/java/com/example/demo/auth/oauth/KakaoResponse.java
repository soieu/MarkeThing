package com.example.demo.auth.oauth;

import com.example.demo.siteuser.entity.SiteUser;
import com.example.demo.type.AuthType;
import java.util.HashMap;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;

@RequiredArgsConstructor
@Getter
public class KakaoResponse implements OAuth2Response {
    private final Map<String, Object> attribute;
    private String userName;
    private String email;
    private String phone;
    private String profileImg;
    private String nickname;
    private String address;
    private Point location;

    @Builder
    public KakaoResponse(Map<String, Object> attribute, String userName, String email, String phone, String profileImg, String nickname, String address, Point location) {
        this.attribute = attribute;
        this.userName = userName;
        this.email = email;
        this.phone = phone;
        this.profileImg = profileImg;
        this.nickname = nickname;
        this.address = address;
        this.location = location;
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return attribute.get("id").toString();
    }

    @Override
    public String getEmail() {
        return this.email;
    }

    @Override
    public String getName() {
        return "";
    }

    public SiteUser toEntity(KakaoResponse kakaoResponse){
        return SiteUser.builder()
                .name(kakaoResponse.getUserName())
                .password("1111")
                .email(kakaoResponse.getEmail())
                .profileImg(kakaoResponse.getProfileImg())
                .nickname(kakaoResponse.getNickname())
                .phoneNumber(kakaoResponse.getPhone())
                .address(kakaoResponse.getAddress())
                .myLocation(kakaoResponse.getLocation())
                .status(true)
                .authType(AuthType.KAKAO)
                .build();
    }
}
