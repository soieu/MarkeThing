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
        List<ChatRoom> agents = chatRoomRepository.findByAgentId(siteUser.getId()); // 대행 구매 희망자였던 채팅방
        List<ChatRoom> requesters = chatRoomRepository.findByRequesterId(siteUser.getId()); // 의뢰자일 때의 채팅방
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


    /**
     * 채팅방을 삭제하는 메소드
     * 현재 자신의 userId에 속해 있는 siteuser의 연관관계를 끊게 되면
     * 다음 자신의 채팅방들을 반환할 때, 화면에 표시가 되지 않는다.
     * Status가 1인 상태에서 채팅의 대화 상대는 채팅 메시지 입력이 비활성되며,
     * deleteChatRoom을 호출하면 해당 채팅방에 속해있는 메시지와 채팅방이 일괄 삭제된다.
     * (채팅 메시지는 MongoDB에 저장이 되기 때문에 삭제 로직 두번 수행.)
     * @param chatRoomId 채팅방의 아이디
     */
    @Override
    @Transactional
    public void deleteChatRoom(Long chatRoomId, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new MarkethingException(CHATROOM_NOT_FOUND));
        if (chatRoom.getAgent().getId() == userId) {
            chatRoom.detachAgent();
        } else {
            chatRoom.detachRequester();
        }
        if(chatRoom.getChatRoomStatus() == 2) {
            chatRoom.minusStatus();
            chatRoomRepository.save(chatRoom); // 변경 사항을 명시적으로 저장
        }
        else { // 두명 이상의 유저가 나갔기 때문에 더 이상 채팅방의 존재가 의미가 없게됨
            chatMessageRepository.deleteByChatRoomId(chatRoomId); // 해당 채팅방에 존재하는 메시지들도 지워짐.
            chatRoomRepository.delete(chatRoom);
        }
    }
    @Override
    public int getChatRoomStatus(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new MarkethingException(CHATROOM_NOT_FOUND));
        return chatRoom.getChatRoomStatus();
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
