package com.example.demo.community.controller.api;

import com.example.demo.common.filter.dto.CommunityFilterRequestDto;
import com.example.demo.community.dto.community.CommunityDetailDto;
import com.example.demo.community.dto.community.CommunityPreviewDto;
import com.example.demo.community.dto.community.CommunityRequestDto;
import com.example.demo.community.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @PatchMapping("/{communityId}")
    public void editCommunity(@RequestBody CommunityRequestDto communityRequestDto
    , @PathVariable Long communityId) {
        String email = "mockEmail@gmail.com";
        communityService.edit(email, communityRequestDto, communityId);
    }

    @DeleteMapping("/{communityId}")
    public void deleteCommunity(@PathVariable Long communityId) {
        String email = "mockEmail@gmail.com";
        communityService.delete(email, communityId);
    }

    @PostMapping("/list")
    public ResponseEntity<Page<CommunityPreviewDto>> getCommunityList(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "5") int size,
            @RequestParam(required = false, defaultValue = "date") String sort,
            @RequestBody(required = false) CommunityFilterRequestDto communityFilterRequestDto) {

        Sort sortOrder = communityService.confirmSortOrder(sort);
        PageRequest pageRequest = PageRequest.of(page, size, sortOrder);

        var result = communityService.getCommunitiesByFilter(
                communityFilterRequestDto.getFilter(), pageRequest);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{communityId}")
    public ResponseEntity<CommunityDetailDto> getCommunityDetail(@PathVariable Long communityId) {
        return ResponseEntity.ok(communityService.getCommunityDetail(communityId));
    }

    @GetMapping("/list/myList")
    public ResponseEntity<Page<CommunityPreviewDto>> getMyCommunityList(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "5") int size,
            @RequestParam(required = false, defaultValue = "date") String sort) {

        String email = "mockEmail@gmail.com";
        Sort sortOrder = communityService.confirmSortOrder(sort);
        PageRequest pageRequest = PageRequest.of(page, size, sortOrder);

        return ResponseEntity.ok(communityService.getMyCommunities(email, pageRequest));
    }

}
