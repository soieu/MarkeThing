package com.example.demo.siteuser.service.impl;

import com.example.demo.exception.MarkethingException;
import com.example.demo.exception.type.ErrorCode;
import com.example.demo.siteuser.dto.SiteUserResponseDto;
import com.example.demo.siteuser.dto.UpdateSiteUserRequestDto;
import com.example.demo.siteuser.entity.SiteUser;
import com.example.demo.siteuser.repository.SiteUserRepository;
import com.example.demo.siteuser.service.SiteUserService;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SiteUserServiceImpl implements SiteUserService {

    private final SiteUserRepository siteUserRepository;

    public SiteUser getSiteUserByEmail(String email) {
        return siteUserRepository.findByEmail(email).orElseThrow(()->new MarkethingException(ErrorCode.USER_NOT_FOUND));
    }
    @Override
    @Transactional
    public void deleteSiteUser(String email) {
        SiteUser siteUser = getSiteUserByEmail(email);
        siteUserRepository.delete(siteUser);
    }

    @Override
    public SiteUserResponseDto getMyInformation(String email) {
        SiteUser siteUser = getSiteUserByEmail(email);
        return SiteUserResponseDto.fromEntity(siteUser);
    }

    @Override
    public void accumulatePoint(String email, int charge) {
        SiteUser siteUser = getSiteUserByEmail(email);
        siteUser.accumulatePoint(charge);
        siteUserRepository.save(siteUser);
    }

    @Override
    public void spendPoint(String email, int charge) {
        SiteUser siteUser = getSiteUserByEmail(email);
        siteUser.spendPoint(charge);
        siteUserRepository.save(siteUser);
    }

    @Override
    @Transactional
    public void updateSiteUser(String email, UpdateSiteUserRequestDto updateSiteUserRequestDto) {
        SiteUser siteUser = siteUserRepository.findByEmail(email).orElseThrow(()->new MarkethingException(ErrorCode.USER_NOT_FOUND));
        String nickName = updateSiteUserRequestDto.getNickname().isBlank()? siteUser.getNickname():updateSiteUserRequestDto.getNickname();
        String phoneNumber = updateSiteUserRequestDto.getPhoneNumber().isBlank()? siteUser.getPhoneNumber():updateSiteUserRequestDto.getPhoneNumber();
        String address = updateSiteUserRequestDto.getAddress().isBlank()? siteUser.getAddress():updateSiteUserRequestDto.getAddress();
        siteUser.updateSiteUser(nickName, phoneNumber, address,
                updateSiteUserRequestDto.getProfileImg());
    }

}
