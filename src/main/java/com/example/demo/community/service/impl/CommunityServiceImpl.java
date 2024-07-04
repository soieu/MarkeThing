package com.example.demo.community.service.impl;

import static com.example.demo.exception.type.ErrorCode.EMAIL_NOT_FOUND;

import com.example.demo.community.dto.CommunityRequestDto;
import com.example.demo.community.entity.Community;
import com.example.demo.community.repository.CommunityRepository;
import com.example.demo.community.service.CommunityService;
import com.example.demo.exception.MarkethingException;
import com.example.demo.siteuser.entity.SiteUser;
import com.example.demo.siteuser.repository.SiteUserRepository;
import lombok.RequiredArgsConstructor;
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
        SiteUser siteUser = siteUserRepository.findByEmail(email)
                .orElseThrow(() -> new MarkethingException(EMAIL_NOT_FOUND));

        return communityRepository.save(communityRequestDto.toEntity(siteUser));
    }
}
