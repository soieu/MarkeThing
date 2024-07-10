package com.example.demo.siteuser.controller.api;

import com.example.demo.siteuser.service.SiteUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class SiteUserApiController {

    private final SiteUserService siteUserService;

    @DeleteMapping()
    public void deleteSiteUser() {
        // TODO: 로그인 된 USER 정보 입력 받아서 삭제하는 걸로 수정 필요!
        Long userId = 1L;
        siteUserService.deleteSiteUser(userId);
    }
}
