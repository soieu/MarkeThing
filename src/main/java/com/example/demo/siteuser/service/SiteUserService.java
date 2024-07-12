package com.example.demo.siteuser.service;

import com.example.demo.siteuser.dto.SiteUserResponseDto;

public interface SiteUserService {
    void deleteSiteUser(String email);
    SiteUserResponseDto getMyInformation(String email);
    void spendPoint(String email, int charge);
}
