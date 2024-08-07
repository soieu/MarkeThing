package com.example.demo.siteuser.dto;

import com.example.demo.siteuser.entity.SiteUser;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SiteUserResponseDto {

    private String email;
    private String name;
    private String nickname;
    private String phoneNumber;
    private String address;
    private List<String> mannerScore;
    private String profileImg;

    @Builder
    public SiteUserResponseDto(String email, String name, String nickname, String phoneNumber, String address, List<String> mannerScore, String profileImg) {
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.mannerScore = mannerScore;
        this.profileImg = profileImg;
    }

    public static SiteUserResponseDto fromEntity(SiteUser siteUser) {
        return SiteUserResponseDto.builder()
                .email(siteUser.getEmail())
                .name(siteUser.getName())
                .nickname(siteUser.getNickname())
                .phoneNumber(siteUser.getPhoneNumber())
                .address(siteUser.getAddress())
                .mannerScore(siteUser.getMannerScore())
                .profileImg(siteUser.getProfileImg())
                .build();
    }
}
