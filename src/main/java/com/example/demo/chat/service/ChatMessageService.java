package com.example.demo.chat.service;

import com.example.demo.chat.dto.ChatMessageRequestDto;


public interface ChatMessageService {
    void sendMessage(ChatMessageRequestDto requestDto);
}