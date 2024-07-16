package com.example.demo.siteuser.controller.view;


import com.example.demo.siteuser.service.SiteUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
@RequiredArgsConstructor
@RequestMapping("/view/users")
public class SiteUserViewController {
    private final SiteUserService siteUserService;

    @GetMapping("/myPage")
    public String getMyPage(){
        return "myPage";
    }
    @GetMapping("/update")
    public String updateSiteUser() { // principal을 이용을 해야하는데 로그인 이없어서 그냥 해보기
        return "updateSiteUser"; //해당 view 이름은 가져 오겄지.?
    }
}
