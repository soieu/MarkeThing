package com.example.demo.community.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CommunityController {
    @GetMapping({"", "/"})
    public String index() {
        return "index";
    }

    @GetMapping("/communities/list")
    public String communitiesList() {
        return "communities/communities";
    }

    @GetMapping("/communities/{communityId}")
    public String communityDetail() {
        return "communities/community";
    }

    @GetMapping("/communities")
    public String newBoard() {
        return "communities/newCommunity";
    }
}
