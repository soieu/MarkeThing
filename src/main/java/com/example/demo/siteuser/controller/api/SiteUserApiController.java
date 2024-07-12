package com.example.demo.siteuser.controller.api;

import com.example.demo.siteuser.dto.MannerRequestDto;
import com.example.demo.siteuser.dto.SiteUserResponseDto;
import com.example.demo.siteuser.dto.UpdateSiteUserRequestDto;
import com.example.demo.siteuser.service.MannerService;
import com.example.demo.siteuser.service.SiteUserService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class SiteUserApiController {

    private final SiteUserService siteUserService;
    private final MannerService mannerService;

    @DeleteMapping()
    public void deleteSiteUser(Principal principal) {
        String email = principal.getName();
        siteUserService.deleteSiteUser(email);
    }

    @GetMapping("/information")
    public ResponseEntity<SiteUserResponseDto> getMyInformation(Principal principal) {
        String email = principal.getName();
        SiteUserResponseDto response = siteUserService.getMyInformation(email);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/{userId}/manner")
    public void createManner(@PathVariable Long userId, @RequestBody MannerRequestDto request) {
        String email = "mockEmail@gmail.com";
        mannerService.createManner(request,email,userId);
    }

    @PatchMapping()
    public void updateSiteUser(@RequestBody UpdateSiteUserRequestDto requestDto, Principal principal) {
        String email = principal.getName();
        siteUserService.updateSiteUser(email, requestDto);

    }

}
