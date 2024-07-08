package com.example.demo.chat.controller;

import com.example.demo.chat.controller.api.ChatRoomApiController;
import com.example.demo.chat.dto.ChatRoomRequestDto;
import com.example.demo.chat.entiity.ChatRoom;
import com.example.demo.chat.service.impl.ChatRoomServiceImpl;
import com.example.demo.marketpurchaserequest.entity.MarketPurchaseRequest;
import com.example.demo.siteuser.entity.SiteUser;
import com.example.demo.type.AuthType;
import com.example.demo.type.PurchaseRequestStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.mockito.ArgumentMatchers.eq;

import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatRoomApiController.class)

public class ChatApiControllerTest {
    @MockBean
    private MappingMongoConverter mappingMongoConverter;
    @MockBean
    private ChatRoomServiceImpl chatRoomServiceImpl;
    @Autowired
    private ObjectMapper objectMapper;
    // objectMapper가 무엇일까?

    @Autowired
    private MockMvc mockMvc;
    // 약간 임시의 객체를 만드는 걸로 알고 있음.

    @Test
    public void createChatRoom() throws Exception {
        ChatRoomRequestDto chatRoomRequestDto = getChatRoomRequestDto(); // 임의로 dto 객체를 생성함
        ChatRoom chatRoom = chatRoomRequestDto.toEntity(getRequest(),getRequester(),getAgent()); // 엔티티를 만들어줌

        String content = objectMapper.writeValueAsString(chatRoomRequestDto); // 입력되는 json 객체를 의미함
        // 입력되는 json 데이터를 String 형으로 변환해줌


        given(chatRoomServiceImpl.createChatRoom(eq(chatRoomRequestDto))).willReturn(chatRoom);

        mockMvc.perform(post("/api/rooms") //api 연결
                        .contentType(MediaType.APPLICATION_JSON) //json 객체를 지정
                        .content(content)) // requestBody에 들어가는 인자 저장
                .andExpect(status().isOk())
                .andDo(print());
    }
    private ChatRoomRequestDto getChatRoomRequestDto(){
        return ChatRoomRequestDto.builder()
                .requestId(1L)
                .requesterId(1L)
                .agentId(2L)
                .build();
    }


    public static MarketPurchaseRequest getRequest(){
        GeometryFactory geometryFactory = new GeometryFactory();
        double longitude = 126.9722919; // 경도
        double latitude = 37.56667062;   // 위도
        Point myLocation = geometryFactory.createPoint(new Coordinate(longitude, latitude));

        return MarketPurchaseRequest.builder()
                .id(1L)
                .title("title")
                .content("content")
                .postImg("postImg")
                .fee(1)
                .purchaseRequestStatus(PurchaseRequestStatus.IN_PROGRESS)
                .meetupTime(LocalDate.now())
                .meetupDate(LocalDate.now())
                .meetupAddress("Address")
                .meetupLocation(myLocation)
                .createdAt(LocalDateTime.now())
                .build();
    }

    private static SiteUser getRequester() {
        GeometryFactory geometryFactory = new GeometryFactory();
        double longitude = 126.9722919; // 경도
        double latitude = 37.56667062;   // 위도
        Point myLocation = geometryFactory.createPoint(new Coordinate(longitude, latitude));

        return SiteUser.builder()
                .id(1L)
                .email("mockEmail@naver.com")
                .password("password")
                .name("name2")
                .nickname("nickname")
                .phoneNumber("010-1234-5678")
                .address("address")
                .myLocation(myLocation)
                .mannerScore(0)
                .profileImg("profileImg")
                .status(true)
                .authType(AuthType.GENERAL)
                .createdAt(LocalDateTime.now())
                .build();
    }



    private static SiteUser getAgent() {
        GeometryFactory geometryFactory = new GeometryFactory();
        double longitude = 126.97796919; // 경도
        double latitude = 37.56667062;   // 위도
        Point myLocation = geometryFactory.createPoint(new Coordinate(longitude, latitude));

        return SiteUser.builder()
                .id(2L)
                .email("mockEmail@gmail.com")
                .password("password")
                .name("name")
                .nickname("nickname")
                .phoneNumber("010-1234-5678")
                .address("address")
                .myLocation(myLocation)
                .mannerScore(0)
                .profileImg("profileImg")
                .status(true)
                .authType(AuthType.GENERAL)
                .createdAt(LocalDateTime.now())
                .build();
    }

}
