package com.example.demo.chat.dto;


import com.example.demo.chat.entiity.ChatRoom;
import com.example.demo.marketpurchaserequest.entity.MarketPurchaseRequest;
import com.example.demo.siteuser.entity.SiteUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@AllArgsConstructor
public class ChatRoomRequestDto {

    private Long requestId;
    private Long requesterId;
    private Long agentId;
    public ChatRoom toEntity(MarketPurchaseRequest request, SiteUser requester, SiteUser agent) {//일단 이렇게 사용을 함
        return ChatRoom.builder()
                .marketPurchaseRequest(request)
                .requester(requester)
                .agent(agent)
                .build();
    }



}
