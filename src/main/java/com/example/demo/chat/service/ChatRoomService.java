package com.example.demo.chat.service;

import com.example.demo.chat.dto.ChatRoomRequestDto;
import com.example.demo.chat.dto.ChatRoomResponseDto;
import com.example.demo.chat.entiity.ChatRoom;
import java.util.List;

public interface ChatRoomService {
    ChatRoom createChatRoom(ChatRoomRequestDto requestDto);

    Long getPurchaseRequest(Long chatRoomId);
    List<ChatRoomResponseDto> getMyChatRooms(Long id); // 내 아이디를 받아서 ChatRoom을 찾음



}
