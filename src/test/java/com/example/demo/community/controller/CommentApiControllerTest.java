package com.example.demo.community.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.community.controller.api.CommentApiController;
import com.example.demo.community.dto.comment.CommentRequestDto;
import com.example.demo.community.entity.Comment;
import com.example.demo.community.entity.Community;
import com.example.demo.community.service.CommentService;
import com.example.demo.community.type.AreaType;
import com.example.demo.config.SecurityConfig;
import com.example.demo.siteuser.entity.SiteUser;
import com.example.demo.type.AuthType;
import com.example.demo.type.PostStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(CommentApiController.class)
@Import(SecurityConfig.class)
public class CommentApiControllerTest {

    @MockBean
    private MappingMongoConverter mappingMongoConverter;

    @MockBean
    private CommentService commentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void createCommentTest() throws Exception {
        // given
        CommentRequestDto commentRequestDto = getCommentRequestDto();
        Community community = getCommunity();
        SiteUser siteUser = getSiteUser();
        Comment comment = getComment(community, siteUser, commentRequestDto);

        String content = objectMapper.writeValueAsString(commentRequestDto);

        given(commentService.create(eq("mockEmail@gmail.com")
                , eq(community.getId()) ,eq(commentRequestDto)))
                .willReturn(comment);

        // when & then
        mockMvc.perform(post("/api/communities/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andDo(print());
    }

    private static Comment getComment(Community community, SiteUser siteUser,
            CommentRequestDto commentRequestDto) {
        return Comment.builder()
                .id(1L)
                .community(community)
                .siteUser(siteUser)
                .content(commentRequestDto.getContent())
                .postStatus(PostStatus.POST)
                .createdAt(LocalDateTime.now())
                .build();
    }

    private static CommentRequestDto getCommentRequestDto() {
        return CommentRequestDto
                .builder()
                .content("content")
                .build();
    }

    private static Community getCommunity() {
        return Community.builder()
                .siteUser(getSiteUser())
                .id(1L)
                .area(AreaType.SEOUL)
                .title("title")
                .content("content")
                .postImg("postImg")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private static SiteUser getSiteUser() {
        GeometryFactory geometryFactory = new GeometryFactory();
        double longitude = 126.97796919; // 경도
        double latitude = 37.56667062;   // 위도
        Point myLocation = geometryFactory.createPoint(new Coordinate(longitude, latitude));

        return SiteUser.builder()
                .id(1L)
                .email("mockEmail@gmail.com")
                .password("password")
                .name("name")
                .nickname("nickname")
                .phoneNumber("010-1234-5678")
                .address("address")
                .myLocation(myLocation)
                .mannerScore(List.of("0,0,0"))
                .profileImg("profileImg")
                .status(true)
                .authType(AuthType.GENERAL)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
