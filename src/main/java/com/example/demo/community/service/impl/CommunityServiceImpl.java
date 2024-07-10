package com.example.demo.community.service.impl;

import static com.example.demo.exception.type.ErrorCode.COMMUNITY_NOT_FOUND;
import static com.example.demo.exception.type.ErrorCode.EMAIL_NOT_FOUND;
import static com.example.demo.exception.type.ErrorCode.UNAUTHORIZED_USER;
import static com.example.demo.exception.type.ErrorCode.USER_NOT_FOUND;

import com.example.demo.common.filter.dto.CommunityFilterDto;
import com.example.demo.community.dto.community.CommunityDetailDto;
import com.example.demo.community.dto.community.CommunityPreviewDto;
import com.example.demo.community.dto.community.CommunityRequestDto;
import com.example.demo.community.entity.Community;
import com.example.demo.community.repository.CommunityRepository;
import com.example.demo.community.service.CommunityService;
import com.example.demo.exception.MarkethingException;
import com.example.demo.siteuser.entity.SiteUser;
import com.example.demo.siteuser.repository.SiteUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService {

    private final CommunityRepository communityRepository;
    private final SiteUserRepository siteUserRepository;

    @Override
    @Transactional
    public Community create(String email, CommunityRequestDto communityRequestDto) {
        var siteUser = siteUserRepository.findByEmail(email)
                .orElseThrow(() -> new MarkethingException(USER_NOT_FOUND));

        return communityRepository.save(communityRequestDto.toEntity(siteUser));
    }

    @Override
    @Transactional
    public Community edit(String email, CommunityRequestDto communityRequestDto, Long communityId) {
        var siteUser = siteUserRepository.findByEmail(email)
                .orElseThrow(() -> new MarkethingException(USER_NOT_FOUND));

        var community = communityRepository.findById(communityId)
                .orElseThrow(() -> new MarkethingException(COMMUNITY_NOT_FOUND));

        validateAuthorization(siteUser, community);
        community.update(communityRequestDto);

        return community;
    }

    @Override
    @Transactional
    public void delete(String email, Long communityId) {
        var siteUser = siteUserRepository.findByEmail(email)
                .orElseThrow(() -> new MarkethingException(USER_NOT_FOUND));

        var community = communityRepository.findById(communityId)
                .orElseThrow(() -> new MarkethingException(COMMUNITY_NOT_FOUND));

        validateAuthorization(siteUser, community);
        communityRepository.delete(community);
    }

    @Override
    public Page<CommunityPreviewDto> getCommunitiesByFilter(CommunityFilterDto communityFilterDto,
            Pageable pageable) {
        if(communityFilterDto.getAreas().isEmpty()) {
            return communityRepository.findAll(pageable)
                    .map(CommunityPreviewDto::fromEntity);
        }
        return communityRepository.findAllByFilter(communityFilterDto, pageable)
                .map(CommunityPreviewDto::fromEntity);
    }

    @Override
    public CommunityDetailDto getCommunityDetail(Long communityId) {
        var community = communityRepository.findById(communityId)
                .orElseThrow(() -> new MarkethingException(COMMUNITY_NOT_FOUND));

        return CommunityDetailDto.fromEntity(community);
    }

    @Override
    public Page<CommunityPreviewDto> getMyCommunities(String email, Pageable pageable) {
        return communityRepository.findAllBySiteUser_email(email, pageable)
                .map(CommunityPreviewDto::fromEntity);
    }

    @Override
    public Sort confirmSortOrder(String sort) {
        Sort sortOrder = Sort.unsorted();
        if("date".equals(sort)) {
            sortOrder = Sort.by("createdAt").descending();
        }
        return sortOrder;
    }

    private static void validateAuthorization(SiteUser siteUser, Community community) {
        if(!siteUser.equals(community.getSiteUser())) {
            throw new MarkethingException(UNAUTHORIZED_USER);
        }
    }
}
