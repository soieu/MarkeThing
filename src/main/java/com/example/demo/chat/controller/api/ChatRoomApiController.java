package com.example.demo.chat.controller.api;


import com.example.demo.chat.dto.ChatRoomRequestDto;
import com.example.demo.chat.service.impl.ChatRoomServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatRoomApiController {
    private final ChatRoomServiceImpl chatRoomServiceImpl;
    //    @GetMapping("/")
//    public ResponseEntity<?> goChatRoom() {
//        List<ChatRoom> chatRooms = chatRepository.findAllRoom();
//        return ResponseEntity.ok(chatRooms);
//    }
    @PostMapping("api/rooms")
    public void createRoom(@RequestBody ChatRoomRequestDto chatRoomRequestDto) {
        chatRoomServiceImpl.createChatRoom(chatRoomRequestDto);
    }//생성을 함.

//    @GetMapping("/userlist")
//    public ArrayList<String> userList(String roomId) {
//        return chatRepository.getUserList(roomId);
//    }

}
