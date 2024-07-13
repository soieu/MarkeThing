package com.example.demo.chat.service;

import com.example.demo.chat.dto.ChatMessageRequestDto;
import com.example.demo.chat.dto.ChatMessageResponseDto;
import java.util.List;


public interface ChatMessageService {
    void sendMessage(ChatMessageRequestDto requestDto);
    List<ChatMessageResponseDto> getChatMessages(Long chatRoomId);
}