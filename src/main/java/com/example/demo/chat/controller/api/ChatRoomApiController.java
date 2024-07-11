package com.example.demo.chat.controller.api;


import com.example.demo.chat.dto.ChatRoomRequestDto;
import com.example.demo.chat.service.ChatRoomService;
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

    @PostMapping("api/rooms")
    public void createRoom(@RequestBody ChatRoomRequestDto chatRoomRequestDto) {
        chatRoomService.createChatRoom(chatRoomRequestDto);
    }//생성을 함.



    // TODO: 헤더 값에서 userId 받아오기 아직은 임의로 지정
    @GetMapping("/api/rooms/{userId}")
    public ResponseEntity<?> getMyChatRooms(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(chatRoomService.getMyChatRooms(userId));
    }

}