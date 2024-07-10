package com.example.demo.siteuser.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SiteUserResponseDto {

    private String email;
    private String name;
    private String phoneNumber;
    private String address;
    private int manner;
    private String profileImg;

}
