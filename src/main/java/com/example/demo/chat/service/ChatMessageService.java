package com.example.demo.chat.service;


import com.example.demo.chat.dto.ChatMessageRequestDto;
import com.example.demo.chat.entiity.ChatMessage;
import com.example.demo.chat.repository.ChatMessageRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    @Transactional
    public void sendMessage(ChatMessageRequestDto requestDto){
        ChatMessage chatMessage = requestDto.toEntity(); // 채팅 메시지를 생성하고 몽고디비에 저장을 함
        chatMessageRepository.save(chatMessage);
    }


}
