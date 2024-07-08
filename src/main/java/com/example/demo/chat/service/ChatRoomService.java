package com.example.demo.chat.service;

import com.example.demo.chat.dto.ChatRoomRequestDto;
import com.example.demo.chat.entiity.ChatRoom;

public interface ChatRoomService {
    ChatRoom createChatRoom(ChatRoomRequestDto requestDto);
}
