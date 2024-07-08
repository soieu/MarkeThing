package com.example.demo.chat.service;


import static com.example.demo.exception.type.ErrorCode.REQUEST_NOT_FOUND;
import static com.example.demo.exception.type.ErrorCode.USER_NOT_FOUND;


import com.example.demo.chat.dto.ChatMessageRequestDto;
import com.example.demo.chat.dto.ChatRoomRequestDto;
import com.example.demo.chat.entiity.ChatMessage;
import com.example.demo.chat.entiity.ChatRoom;
import com.example.demo.chat.repository.ChatMessageRepository;
import com.example.demo.chat.repository.ChatRoomRepository;
import com.example.demo.exception.MarkethingException;
import com.example.demo.marketpurchaserequest.entity.MarketPurchaseRequest;
import com.example.demo.marketpurchaserequest.repository.MarketPurchaseRequestRepository;
import com.example.demo.siteuser.entity.SiteUser;
import com.example.demo.siteuser.repository.SiteUserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final SiteUserRepository siteUserRepository;
    private final MarketPurchaseRequestRepository requestRepository;
    private final ChatMessageRepository chatMessageRepository;



    @Transactional
    public ChatRoom createChatRoom(ChatRoomRequestDto chatRoomRequestDto) {
        SiteUser requester = siteUserRepository.findById(chatRoomRequestDto.getRequesterId())
                .orElseThrow(()-> new MarkethingException(USER_NOT_FOUND));

        SiteUser agent = siteUserRepository.findById(chatRoomRequestDto.getAgentId())
                .orElseThrow(() -> new MarkethingException(USER_NOT_FOUND));

        MarketPurchaseRequest request = requestRepository.findById(chatRoomRequestDto.getRequestId())
                .orElseThrow(() -> new MarkethingException(REQUEST_NOT_FOUND));

        ChatRoom chatRoom = chatRoomRequestDto.toEntity(request, agent, requester);

        return chatRoomRepository.save(chatRoom);
    }

    @Transactional
    public void sendMessage(ChatMessageRequestDto requestDto) {
        ChatMessage chatMessage = requestDto.toEntity();
        chatMessageRepository.save(chatMessage);
        //메시지를 생성을하고 디비에 저장을 함.
    }
}
