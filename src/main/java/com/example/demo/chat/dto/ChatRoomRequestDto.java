package com.example.demo.chat.dto;


import com.example.demo.chat.entiity.ChatRoom;
import com.example.demo.marketpurchaserequest.entity.MarketPurchaseRequest;
import com.example.demo.siteuser.entity.SiteUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ChatRoomRequestDto {
    public ChatRoom toEntity(SiteUser requester
            ,SiteUser agent,MarketPurchaseRequest request) {//일단 이렇게 사용을 함
        return ChatRoom.builder()
                .requester(requester)
                .agent(agent)
                .purchaseRequest(request)
                .build();
    }



}
