package com.example.demo.siteuser.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UpdateSiteUserRequestDto {

    private String nickname;
    private String phoneNumber;
    private String address;
    private String profileImg;

    @Builder
    public UpdateSiteUserRequestDto(String phoneNumber, String nickName, String address, String profileImg) {
        this.nickname = nickName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.profileImg = profileImg;
    }
}
