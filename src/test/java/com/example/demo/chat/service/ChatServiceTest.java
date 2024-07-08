package com.example.demo.chat.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.BDDMockito.given;

import com.example.demo.chat.dto.ChatRoomRequestDto;
import com.example.demo.chat.entiity.ChatRoom;
import com.example.demo.chat.repository.ChatRoomRepository;
import com.example.demo.community.entity.Community;
import com.example.demo.community.repository.CommunityRepository;
import com.example.demo.community.service.impl.CommunityServiceImpl;
import com.example.demo.exception.MarkethingException;
import com.example.demo.exception.type.ErrorCode;
import com.example.demo.marketpurchaserequest.entity.MarketPurchaseRequest;
import com.example.demo.marketpurchaserequest.repository.MarketPurchaseRequestRepository;
import com.example.demo.siteuser.entity.SiteUser;
import com.example.demo.siteuser.repository.SiteUserRepository;
import com.example.demo.type.AuthType;
import com.example.demo.type.PurchaseRequestStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

@ExtendWith(MockitoExtension.class)
public class ChatServiceTest {

    @MockBean
    private MappingMongoConverter mappingMongoConverter;

    @Mock
    private SiteUserRepository siteUserRepository;

    @Mock
    private MarketPurchaseRequestRepository requestRepository;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @InjectMocks
    private ChatRoomService chatRoomService;

    @Test
    void createSuccess() {
        ChatRoomRequestDto chatRoomRequestDto = getChatRoomRequestDto();
        MarketPurchaseRequest request = getRequest();
        SiteUser requester = getRequester();
        SiteUser agent = getAgent();
        ChatRoom chatRoom = chatRoomRequestDto.toEntity(request, requester, agent);


        // given --> 이거로써 지정을 해주는 것
        given(requestRepository.findById(chatRoomRequestDto.getRequestId())) //
                .willReturn(Optional.of(request));
        given(siteUserRepository.findById(chatRoomRequestDto.getRequesterId()))
                .willReturn(Optional.of(requester));
        given(siteUserRepository.findById(chatRoomRequestDto.getAgentId()))
                .willReturn(Optional.of(agent));
        given(chatRoomRepository.save(any(ChatRoom.class)))
                .willReturn(chatRoom);

        // when
        ChatRoom result = chatRoomService.createChatRoom(chatRoomRequestDto);

        // then
        assertThat(result.getAgent()).isEqualTo(agent);
        assertThat(result.getMarketPurchaseRequest()).isEqualTo(request);
        assertThat(result.getRequester()).isEqualTo(requester);
    }

    @Test
    void createFailedByNotFound() {
        ChatRoomRequestDto requestDto = getChatRoomRequestDto();
        // given
        given(requestRepository.findById(requestDto.getRequesterId())).willReturn(Optional.empty());

        // when
        MarkethingException exception = assertThrows(MarkethingException.class,
                () -> chatRoomService.createChatRoom(requestDto));

        // then
        assertEquals(exception.getErrorCode(), ErrorCode.USER_NOT_FOUND);
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
