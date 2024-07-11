package com.example.demo.chat.dto;


import com.example.demo.chat.entiity.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ChatRoomResponseDto {

    private Long chatRoomId; // 채팅방의 아이디를 지정
    private String title; // 의뢰글의 제목을 저장함
    private String lastChatMessage; // 시간상 마지막 메시지를 저장해서 화면에 표시함
    private String time; // 몽고 디비에 저장이 되어 있는 시간을 화면에 표시하기 위한 필드 [오전|오후] HH:MM
    public static ChatRoomResponseDto fromEntity(ChatRoom chatRoom, String lastChatMessage, String time) {
        return ChatRoomResponseDto.builder()
                .chatRoomId(chatRoom.getId())
                .title(chatRoom.getMarketPurchaseRequest().getTitle())
                .lastChatMessage(lastChatMessage)
                .time(time)
                .build();
    }
}
