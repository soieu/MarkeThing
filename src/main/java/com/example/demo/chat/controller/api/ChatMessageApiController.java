package com.example.demo.chat.controller.api;


import com.example.demo.chat.dto.ChatMessageRequestDto;
import com.example.demo.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatMessageApiController {

    private final ChatMessageService chatMessageService;
    private final SimpMessageSendingOperations template;

    @MessageMapping("/sendMessage") // 채팅방에 입장을 하게 되면 생성 --> 메시지를 전송을 하게 됨.
    public void sendMessage(@Payload ChatMessageRequestDto requestDto, SimpMessageHeaderAccessor headerAccessor) {
        //String userUUID = repository.addUser(chat.getRoomId(), chat.getSender());
        //headerAccessor.getSessionAttributes().put("userUUID", userUUID);
        headerAccessor.getSessionAttributes().put("chatRoomId", requestDto.getChatRoomId());
        chatMessageService.sendMessage(requestDto);

        template.convertAndSend("/sub/chat/room/" + requestDto.getChatRoomId(), requestDto); // 이제 보내지는 것이겠죠?
    }
}
