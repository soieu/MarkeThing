package com.example.demo.chat.service.impl;
import static com.example.demo.exception.type.ErrorCode.CHATROOM_NOT_FOUND;
import static com.example.demo.exception.type.ErrorCode.REQUEST_NOT_FOUND;
import static com.example.demo.exception.type.ErrorCode.USER_NOT_FOUND;

import com.example.demo.chat.dto.ChatRoomRequestDto;
import com.example.demo.chat.dto.ChatRoomResponseDto;
import com.example.demo.chat.entiity.ChatMessage;
import com.example.demo.chat.entiity.ChatRoom;
import com.example.demo.chat.repository.ChatMessageRepository;
import com.example.demo.chat.repository.ChatRoomRepository;
import com.example.demo.chat.service.ChatRoomService;
import com.example.demo.exception.MarkethingException;
import com.example.demo.marketpurchaserequest.entity.MarketPurchaseRequest;
import com.example.demo.marketpurchaserequest.repository.MarketPurchaseRequestRepository;
import com.example.demo.siteuser.entity.SiteUser;
import com.example.demo.siteuser.repository.SiteUserRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomServiceImpl implements ChatRoomService{

    private final ChatRoomRepository chatRoomRepository;
    private final SiteUserRepository siteUserRepository;
    private final MarketPurchaseRequestRepository requestRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Override
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

    @Override
    @Transactional
    public Long getPurchaseRequest(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new MarkethingException(CHATROOM_NOT_FOUND));
        return chatRoom.getMarketPurchaseRequest().getId();
    }

    @Override
    @Transactional
    public List<ChatRoomResponseDto> getMyChatRooms(Long id) { //나의 아이디를 가져오는 거겠지
        SiteUser siteUser = siteUserRepository.findById(id)
                .orElseThrow(() -> new MarkethingException(USER_NOT_FOUND));


        List<ChatRoom> agents = chatRoomRepository.findByAgent(siteUser); // 대행 구매 희망자였던 채팅방
        List<ChatRoom> requesters = chatRoomRepository.findByRequester(siteUser); // 의뢰자일 때의 채팅방
        // 두 리스트를 합치고 중복을 제거하기 위한 TreeSet -> 생성순으로 오름차순으로 정렬을 함
        TreeSet<ChatRoom> chatRoomTreeSet = new TreeSet<>(Comparator.comparing(ChatRoom::getCreatedAt));

        chatRoomTreeSet.addAll(agents);
        chatRoomTreeSet.addAll(requesters);

        //내가 의뢰를 했거나 참여를 하게된 request들이 반환이 됨 --> Set에
        List<ChatRoomResponseDto> chatRoomResponseDtos = new ArrayList<>();
        for (ChatRoom chatRoom : chatRoomTreeSet) {
            ChatMessage lastMessage = chatMessageRepository.findFirstByChatRoomIdOrderByCreatedAtDesc(
                    chatRoom.getId()); // 채팅 메시지에서 가장 최근에 저장된 메시지를 반환을 해줌
            String lastChatMessage = (lastMessage != null) ? lastMessage.getContent() : ""; //아직 저장이 채팅이 없으면 공백처리
            String time = (lastMessage != null) ? getFormattedTime(lastMessage.getCreatedAt()) : "";
            chatRoomResponseDtos.add(ChatRoomResponseDto.fromEntity(chatRoom,lastChatMessage,time));
        }
        return chatRoomResponseDtos;
    }
    /*
    LocalDateTime을 hh:mm a 형식으로 반환해주는 메소드를 의미 이때 a는 오전, 오후가 표시됨
    Local.KOREA --> (오전, 오후) Local.ENGLISH --> (AM, PM)
     */
    private String getFormattedTime(LocalDateTime createdAt) {
        if(createdAt == null) return "";
        // DateTimeFormatter to output the time in desired format
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.KOREA);
        // Format the createdAt to desired format
        return createdAt.format(outputFormatter);
    }
}
