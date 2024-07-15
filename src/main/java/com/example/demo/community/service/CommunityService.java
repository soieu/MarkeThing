package com.example.demo.community.service;

import com.example.demo.common.filter.dto.community.CommunityFilterDto;
import com.example.demo.community.dto.community.CommunityDetailDto;
import com.example.demo.community.dto.community.CommunityPreviewDto;
import com.example.demo.community.dto.community.CommunityRequestDto;
import com.example.demo.community.entity.Community;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface CommunityService {

    Community create(String email, CommunityRequestDto communityRequestDto);

    Community edit(String email, CommunityRequestDto communityRequestDto, Long communityId);

    void delete(String email, Long communityId);

    Page<CommunityPreviewDto> getCommunitiesByFilter(CommunityFilterDto communityFilterDto,
            Pageable pageable);

    CommunityDetailDto getCommunityDetail(Long communityId);

    Page<CommunityPreviewDto> getMyCommunities(String email, Pageable pageable);

    Sort confirmSortOrder(String sort);
}
