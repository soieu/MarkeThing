package com.example.demo.chat.dto;

import com.example.demo.chat.entiity.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ChatMessageRequestDto {

    private Long chatRoomId;
    private Long senderId; // 발신자 아이디
    private String content; // 내용

    public ChatMessage toEntity() {//일단 이렇게 사용을 함
        return ChatMessage.builder()
                .chatRoomId(chatRoomId)
                .senderId(senderId)
                .content(content)
                .build();
    }
}
