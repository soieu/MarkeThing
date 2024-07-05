package com.example.demo.community.service;

import com.example.demo.community.dto.CommunityRequestDto;
import com.example.demo.community.entity.Community;

public interface CommunityService {

    Community create(String email, CommunityRequestDto communityRequestDto);

    Community edit(String email, CommunityRequestDto communityRequestDto, Long communityId);

    void delete(String email, Long communityId);
}
