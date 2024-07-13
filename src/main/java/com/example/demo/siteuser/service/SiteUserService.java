package com.example.demo.siteuser.service;

import com.example.demo.siteuser.dto.SiteUserResponseDto;
import com.example.demo.siteuser.dto.UpdateSiteUserRequestDto;

public interface SiteUserService {
    void deleteSiteUser(String email);
    SiteUserResponseDto getMyInformation(String email);
    void updateSiteUser(String email, UpdateSiteUserRequestDto updateSiteUserRequestDto);
}
