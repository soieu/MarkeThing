package com.example.demo.siteuser.service.impl;

import com.example.demo.siteuser.repository.SiteUserRepository;
import com.example.demo.siteuser.service.SiteUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SiteUserServiceImpl implements SiteUserService {

    private final SiteUserRepository siteUserRepository;

    @Override
    public void deleteSiteUser(long id) {
        siteUserRepository.deleteById(id);
    }
}
