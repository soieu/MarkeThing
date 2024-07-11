package com.example.demo.chat.dto;


import com.example.demo.chat.entiity.ChatMessage;
import com.example.demo.chat.entiity.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ChatMessageResponseDto {
    private Long senderId;
    private String content; // 내용
    private String time; // 전송 시

    public static ChatMessageResponseDto fromEntity(ChatMessage chatMessage, String time) {
        return ChatMessageResponseDto.builder()
                .senderId(chatMessage.getSenderId())
                .content(chatMessage.getContent())
                .time(time)
                .build();
    }
}
