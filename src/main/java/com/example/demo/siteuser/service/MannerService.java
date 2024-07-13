package com.example.demo.siteuser.service;

import com.example.demo.siteuser.entity.Manner;
import com.example.demo.siteuser.dto.MannerRequestDto;

public interface MannerService {
    Manner createManner(MannerRequestDto requestDto, String email, Long userId);

}