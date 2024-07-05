package com.example.demo.chat.controller.api;


import com.example.demo.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatRoomApiController {
    private final ChatRoomService chatRoomService;
    //    @GetMapping("/")
//    public ResponseEntity<?> goChatRoom() {
//        List<ChatRoom> chatRooms = chatRepository.findAllRoom();
//        return ResponseEntity.ok(chatRooms);
//    }
    @PostMapping("api/room")
    public void createRoom(@RequestParam("request") Long requestId
    ,@RequestParam("requester") Long requesterId,@RequestParam("agent") Long agentId) {

        System.out.println(requesterId);
        chatRoomService.createChatRoom(requestId, requesterId, agentId);
    }//생성을 함.

//    @GetMapping("/userlist")
//    public ArrayList<String> userList(String roomId) {
//        return chatRepository.getUserList(roomId);
//    }

}
