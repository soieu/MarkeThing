package com.example.demo.chat.controller.view;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatMessageViewController {
    @GetMapping(value = "test/chat1")
    public String jsp1(){
        return "chatRoom";
    }

    @GetMapping(value = "test/chat2")
    public String jsp2(){
        return "chatRoom2";
    }


}
