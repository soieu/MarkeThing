package com.example.demo.siteuser.service;

import com.example.demo.siteuser.dto.SiteUserResponseDto;
import com.example.demo.siteuser.dto.UpdateSiteUserRequestDto;

public interface SiteUserService {
    void deleteSiteUser(String email);
    SiteUserResponseDto getMyInformation(String email);
    void accumulatePoint(String email, int charge);
    void spendPoint(String email, int charge);
    void updateSiteUser(String email, UpdateSiteUserRequestDto updateSiteUserRequestDto);
}
