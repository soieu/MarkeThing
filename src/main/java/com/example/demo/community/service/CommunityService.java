package com.example.demo.community.service;

import com.example.demo.common.filter.dto.CommunityFilterDto;
import com.example.demo.community.dto.CommunityPreviewDto;
import com.example.demo.community.dto.CommunityRequestDto;
import com.example.demo.community.entity.Community;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommunityService {

    Community create(String email, CommunityRequestDto communityRequestDto);

    Community edit(String email, CommunityRequestDto communityRequestDto, Long communityId);

    void delete(String email, Long communityId);

    Page<CommunityPreviewDto> getCommunityByFilter(CommunityFilterDto communityFilterDto,
            Pageable pageable);
}
