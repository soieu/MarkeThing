package com.example.demo.chat.controller.api;


import com.example.demo.chat.dto.ChatMessageRequestDto;
import com.example.demo.chat.entiity.ChatMessage;
import com.example.demo.chat.service.ChatRoomService;
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

    private final ChatRoomService chatRoomService;
    private final SimpMessageSendingOperations template;

    @MessageMapping("/sendMessage") // 채팅방에 입장을 하게 되면 생성 --> 메시지를 전송을 하게 됨.
    public void sendMessage(@Payload ChatMessageRequestDto requestDto, SimpMessageHeaderAccessor headerAccessor) {
        //String userUUID = repository.addUser(chat.getRoomId(), chat.getSender());
        //headerAccessor.getSessionAttributes().put("userUUID", userUUID);
        headerAccessor.getSessionAttributes().put("chatRoomId", requestDto.getChatRoomId());

        chatRoomService.sendMessage(requestDto);
        //그러면 이제 서비스에서 채팅을 저하는 기능을 해야겠네요?
        //chat은 화면에 표시가 되는 것이고 디비에 저장하는 것은 따로 만들어 버리기.
        template.convertAndSend("/sub/chat/room/" + requestDto.getChatRoomId(), requestDto); // 이제 보내지는 것이겠죠?
    }

//    @MessageMapping("/send-message") // 채팅방에 입장을 하게 되면 생성 --> 메시지를 전송을 하게 됨.
//    public void sendMessage(@Payload ChatDto chat, SimpMessageHeaderAccessor headerAccessor) {
//
//        headerAccessor.getSessionAttributes().put("roomId", chat.getRoomId());
//        System.out.println(chat.getMessage());
//        //그러면 이제 서비스에서 채팅을 저하는 기능을 해야겠네요?
//        repository.sendMessage(chat); // 될려나?
//
//        //chat은 화면에 표시가 되는 것이고 디비에 저장하는 것은 따로 만들어 버리기.
//        template.convertAndSend("/sub/chat/room/" + chat.getRoomId(),chat); // 이제 보내지는 것이겠죠?
//    }



}
