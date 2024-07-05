package com.example.demo.chat.service;


import static com.example.demo.exception.type.ErrorCode.REQUEST_NOT_FOUND;
import static com.example.demo.exception.type.ErrorCode.USER_NOT_FOUND;


import com.example.demo.chat.dto.ChatRoomRequestDto;
import com.example.demo.chat.entiity.ChatRoom;
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



    @Transactional
    public ChatRoom createChatRoom(Long requestId, Long requesterId, Long agentId) {


        SiteUser requester = siteUserRepository.findById(requesterId)
                .orElseThrow(()-> new MarkethingException(USER_NOT_FOUND));
        SiteUser agent = siteUserRepository.findById(agentId)
                .orElseThrow(() -> new MarkethingException(USER_NOT_FOUND));
        MarketPurchaseRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new MarkethingException(REQUEST_NOT_FOUND));

        ChatRoomRequestDto chatRoomRequestDto = new ChatRoomRequestDto();
        ChatRoom chatRoom = chatRoomRequestDto.toEntity(requester, agent, request);
        return chatRoomRepository.save(chatRoom);
    }

}
