package com.example.demo.chat.controller.api;


import com.example.demo.chat.dto.ChatMessageResponseDto;
import com.example.demo.chat.dto.ChatRoomRequestDto;
import com.example.demo.chat.service.ChatMessageService;
import com.example.demo.chat.service.ChatRoomService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatRoomApiController {
    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;
    // TODO: requester, request, agent 아이디를 어떻게 전송을 해야하는 지 띵킹
    @PostMapping("api/chat/rooms")
    public void createRoom(@RequestBody ChatRoomRequestDto chatRoomRequestDto) {
        chatRoomService.createChatRoom(chatRoomRequestDto);
    }//생성을 함.
    @GetMapping(value = "api/chat/rooms/{chatRoomId}/user/{userId}")
    public List<ChatMessageResponseDto> getChatRoom(@PathVariable("chatRoomId") Long chatRoomId, @PathVariable("userId") Long userId){
        List<ChatMessageResponseDto> chatMessageDtos = chatMessageService.getChatMessages(chatRoomId);
        return chatMessageDtos;
    }
    // TODO: 헤더 값에서 email을 받아와서 userId를 넣어줌
    @GetMapping("/api/chat/rooms/{userId}")
    public ResponseEntity<?> getMyChatRooms(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(chatRoomService.getMyChatRooms(userId));
    }
}