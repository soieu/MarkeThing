package com.example.demo.common.filter.repository.community;

import com.example.demo.common.filter.dto.CommunityFilterDto;
import com.example.demo.community.entity.Community;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommunityFilteringRepository {
    Page<Community> findAllByFilter(CommunityFilterDto communityFilterDto, Pageable pageable);
}
