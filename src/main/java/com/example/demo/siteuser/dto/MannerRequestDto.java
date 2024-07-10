package com.example.demo.siteuser.dto;


import com.example.demo.entity.Manner;
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

    public Manner toEntity(SiteUser requester, SiteUser agent) {
        return Manner.builder()
                .requester(requester)
                .agent(agent)
                .rate(rate)
                .build();
    }
}
