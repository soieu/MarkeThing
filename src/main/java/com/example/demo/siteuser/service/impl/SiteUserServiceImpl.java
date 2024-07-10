package com.example.demo.siteuser.service.impl;

import com.example.demo.exception.MarkethingException;
import com.example.demo.exception.type.ErrorCode;
import com.example.demo.siteuser.entity.SiteUser;
import com.example.demo.siteuser.repository.SiteUserRepository;
import com.example.demo.siteuser.service.SiteUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SiteUserServiceImpl implements SiteUserService {

    private final SiteUserRepository siteUserRepository;

    @Override
    public void deleteSiteUser(String email) {
        SiteUser siteUser = siteUserRepository.findByEmail(email).orElseThrow(()-> new MarkethingException(
                ErrorCode.USER_NOT_FOUND));
        siteUserRepository.delete(siteUser);
    }
}
