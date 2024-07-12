package com.example.demo.marketpurchaserequest.controller.view;

import com.example.demo.common.kakao.KakaoLocalService;
import com.example.demo.marketpurchaserequest.service.MarketPurchaseRequestService;
import com.example.demo.siteuser.service.impl.SiteUserServiceImpl;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MarketPurchaseRequestController {

    private final SiteUserServiceImpl siteUserServiceImpl;
    private final MarketPurchaseRequestService marketPurchaseRequestService;
    private final KakaoLocalService kakaoLocalService;

    @Value("${kakao.kakao-js-key}")
    String kakaoJSKey;

    @GetMapping("/request/regRequest")
    public String reqRequest(Model model) throws UnsupportedEncodingException, URISyntaxException {
        String email = "MockEmail@gmail.com";
        String address = siteUserServiceImpl.getSiteUserByEmail(email).getAddress();
        double[] latlon = kakaoLocalService.getCoord(address);
        model.addAttribute("kakaoMapKey", kakaoJSKey);
        model.addAttribute("latlon", latlon);
        return "request/regRequest";
    }
}
