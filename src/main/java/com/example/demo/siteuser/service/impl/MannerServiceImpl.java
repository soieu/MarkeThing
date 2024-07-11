package com.example.demo.siteuser.service.impl;

import com.example.demo.siteuser.entity.Manner;
import com.example.demo.exception.MarkethingException;
import com.example.demo.exception.type.ErrorCode;
import com.example.demo.siteuser.dto.MannerRequestDto;
import com.example.demo.siteuser.entity.SiteUser;
import com.example.demo.siteuser.repository.MannerRepository;
import com.example.demo.siteuser.repository.SiteUserRepository;
import com.example.demo.siteuser.service.MannerService;
import com.example.demo.type.Rate;
import java.util.Arrays;
import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MannerServiceImpl implements MannerService {

    private final MannerRepository managerRepository;
    private final SiteUserRepository siteUserRepository;

    @Override
    @Transactional
    public Manner createManner(MannerRequestDto mannerRequestDto, String email, Long userId) {
        SiteUser rater = siteUserRepository.findByEmail(email).orElseThrow(() -> new MarkethingException(
                ErrorCode.USER_NOT_FOUND));
        SiteUser taker = siteUserRepository.findById(userId).orElseThrow(() -> new MarkethingException(ErrorCode.USER_NOT_FOUND));
        checkManner(taker, mannerRequestDto.getRate());
        taker.updateManner(checkManner(taker, mannerRequestDto.getRate()));
        return managerRepository.save(mannerRequestDto.toEntity(rater, taker));
    }

    public List<String> checkManner(SiteUser siteUser, Rate rate) {
        String manner = siteUser.getMannerScore().toString();
        manner = manner.substring(1,manner.length()-1);
        String[] results = manner.replaceAll("[\\t\\s]","").split(",");
        if(rate.equals(Rate.GOOD))
            results[0] = countManner(Integer.parseInt(results[0]));
        if(rate.equals(Rate.NORMAL))
            results[1] = countManner(Integer.parseInt(results[1]));
        if(rate.equals(Rate.BAD))
            results[2] = countManner(Integer.parseInt(results[2]));

        return Arrays.asList(results);
    }
    public static String countManner(Integer count) {
        return String.valueOf(count+1);
    }
}
