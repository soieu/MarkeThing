package com.example.demo.siteuser.dto;


import com.example.demo.siteuser.entity.Manner;
import com.example.demo.siteuser.entity.SiteUser;
import com.example.demo.type.Rate;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MannerRequestDto {

    private Long agentId;
    private Long requesterId;
    private Rate rate;

    @Builder
    public MannerRequestDto(Long agentId, Long requesterId, Rate rate) {
        this.agentId = agentId;
        this.requesterId = requesterId;
        this.rate = rate;
    }

    public Manner toEntity(SiteUser rater, SiteUser taker) {
        return Manner.builder()
                .rater(rater)
                .taker(taker)
                .rate(rate)
                .build();
    }
}
