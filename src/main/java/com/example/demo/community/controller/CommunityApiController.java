package com.example.demo.community.controller;

import com.example.demo.community.dto.CommunityRequestDto;
import com.example.demo.community.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/communities")
public class CommunityApiController {
    private final CommunityService communityService;

    // 회원 가입 기능 구현 완료 후 user 정보 가져오기 위해 Principal 객체 request에 추가
    @PostMapping
    public void postCommunity(@RequestBody CommunityRequestDto communityRequestDto) {
        String email = "mockEmail@gmail.com";
        communityService.create(email, communityRequestDto);
    }

}
