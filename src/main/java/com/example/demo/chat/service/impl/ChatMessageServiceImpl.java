package com.example.demo.chat.service.impl;

import static com.example.demo.exception.type.ErrorCode.CHATROOM_NOT_FOUND;
import com.example.demo.chat.dto.ChatMessageRequestDto;
import com.example.demo.chat.dto.ChatMessageResponseDto;
import com.example.demo.chat.entiity.ChatMessage;
import com.example.demo.chat.entiity.ChatRoom;
import com.example.demo.chat.repository.ChatMessageRepository;
import com.example.demo.chat.repository.ChatRoomRepository;
import com.example.demo.chat.service.ChatMessageService;
import com.example.demo.exception.MarkethingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    @Transactional
    public void sendMessage(ChatMessageRequestDto requestDto){
        ChatMessage chatMessage = requestDto.toEntity(); // 채팅 메시지를 생성하고 몽고디비에 저장을 함
        chatMessageRepository.save(chatMessage);
    }


    /*
    chatRoomId를 통해 모든 메시지를 가져오고 형식에 맞게 dto리스트로 변환
     */
    @Override
    @Transactional
    public List<ChatMessageResponseDto> getChatMessages(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new MarkethingException(CHATROOM_NOT_FOUND));

        List<ChatMessage> chatMessageList = chatMessageRepository.findByChatRoomId(
                chatRoom.getId());

        List<ChatMessageResponseDto> chatMessageResponseDtos = new ArrayList<>();

        for (ChatMessage chatMessage : chatMessageList) {
            ChatMessageResponseDto responseDto = ChatMessageResponseDto.fromEntity(chatMessage,getFormattedTime(chatMessage.getCreatedAt()));
            chatMessageResponseDtos.add(responseDto);
        }
        return chatMessageResponseDtos;
    }

    private String getFormattedTime(LocalDateTime createdAt) {
        if(createdAt == null) return "";
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.KOREA);
        return createdAt.format(outputFormatter);
    }
}
