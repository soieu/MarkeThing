package com.example.demo.chat.controller.view;

import com.example.demo.chat.dto.ChatMessageResponseDto;
import com.example.demo.chat.dto.ChatRoomResponseDto;
import com.example.demo.chat.service.ChatMessageService;
import com.example.demo.chat.service.ChatRoomService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RequiredArgsConstructor
@Controller
public class ChatRoomViewController {
    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;


    // TODO: 헤더 값에서 userId 받아오기 아직은 임의로 지정
    @GetMapping("/view/chat/rooms/{userId}")
    public String myChatRoom(@PathVariable("userId") Long userId, Model model) {
        List<ChatRoomResponseDto> chatRooms = chatRoomService.getMyChatRooms(userId);
        model.addAttribute("chatRooms", chatRooms);
        model.addAttribute("userId", userId);
        return "chatRooms";
    }
    // TODO: 헤더 값에서 userId 받아오기 아직은 임의로 지정
    @GetMapping(value = "view/chat/rooms/{chatRoomId}/user/{userId}")
    public String getChatRoom(@PathVariable("chatRoomId") Long chatRoomId, @PathVariable("userId") Long userId, Model model){
        List<ChatMessageResponseDto> chatMessageDtos = chatMessageService.getChatMessages(chatRoomId);
        Long requestId = chatRoomService.getPurchaseRequest(chatRoomId);
        int  chatRoomStatus = chatRoomService.getChatRoomStatus(chatRoomId);
        model.addAttribute("chatRoomId",chatRoomId);
        model.addAttribute("requestId", requestId);
        model.addAttribute("userId", userId);
        model.addAttribute("chatMessages", chatMessageDtos);
        model.addAttribute("chatRoomStatus", chatRoomStatus);
        return "chatRoom";
    }
}
