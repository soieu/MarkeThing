package com.example.demo.siteuser.service;

import com.example.demo.siteuser.dto.MannerRequestDto;

public interface MannerService {
    void createManner(MannerRequestDto requestDto, String email, Long userId);

}