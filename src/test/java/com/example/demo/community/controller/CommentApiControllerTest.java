package com.example.demo.community.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.auth.jwt.JWTUtil;
import com.example.demo.community.controller.api.CommentApiController;
import com.example.demo.community.dto.comment.CommentRequestDto;
import com.example.demo.community.dto.comment.ReplyCommentRequestDto;
import com.example.demo.community.entity.Comment;
import com.example.demo.community.entity.Community;
import com.example.demo.community.entity.ReplyComment;
import com.example.demo.community.service.CommentService;
import com.example.demo.community.service.ReplyCommentService;
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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(CommentApiController.class)
@AutoConfigureMockMvc
public class CommentApiControllerTest {

    @MockBean
    private MappingMongoConverter mappingMongoConverter;

    @MockBean
    private CommentService commentService;

    @MockBean
    private ReplyCommentService replyCommentService;

    @MockBean
    private JWTUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "mockEmail@gmail.com")
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
                        .content(content).with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "mockEmail@gmail.com")
    public void editCommentTest() throws Exception {
        // given
        CommentRequestDto commentRequestDto = getCommentRequestDto();
        Community community = getCommunity();
        SiteUser siteUser = getSiteUser();
        Comment updatedComment = getUpdatedComment(community, siteUser, commentRequestDto);

        String content = objectMapper.writeValueAsString(commentRequestDto);

        given(commentService.edit(eq("mockEmail@gmail.com")
                , eq(community.getId()) ,eq(commentRequestDto)))
                .willReturn(updatedComment);

        // when & then
        mockMvc.perform(patch("/api/communities/comments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content).with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "mockEmail@gmail.com")
    public void deleteCommentTest() throws Exception {
        // given
        Community community = getCommunity();
        SiteUser siteUser = getSiteUser();
        Comment deleteComment = getDeletedComment(community, siteUser);

        given(commentService.delete(eq("mockEmail@gmail.com")
                , eq(community.getId())))
                .willReturn(deleteComment);

        // when & then
        mockMvc.perform(delete("/api/communities/comments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "mockEmail@gmail.com")
    public void createReplyCommentTest() throws Exception {
        // given
        CommentRequestDto commentRequestDto = getCommentRequestDto();
        ReplyCommentRequestDto replyCommentRequestDto = getReplyCommentRequestDto();
        SiteUser siteUser = getSiteUser();
        Community community = getCommunity();
        Comment comment = getComment(community, siteUser, commentRequestDto);

        ReplyComment replyComment = getReplyComment(comment, siteUser, replyCommentRequestDto);

        String content = objectMapper.writeValueAsString(commentRequestDto);

        given(replyCommentService.create(eq("mockEmail@gmail.com")
                , eq(community.getId()) ,eq(replyCommentRequestDto)))
                .willReturn(replyComment);

        // when & then
        mockMvc.perform(post("/api/communities/comments/1/replyComments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content).with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    private static ReplyComment getReplyComment(Comment comment, SiteUser siteUser,
            ReplyCommentRequestDto replyCommentRequestDto) {
        return ReplyComment.builder()
                .id(1L)
                .comment(comment)
                .siteUser(siteUser)
                .content(replyCommentRequestDto.getContent())
                .postStatus(PostStatus.POST)
                .createdAt(LocalDateTime.now())
                .build();
    }

    private static ReplyCommentRequestDto getReplyCommentRequestDto() {
        return ReplyCommentRequestDto
                .builder()
                .content("content")
                .build();
    }

    private static Comment getUpdatedComment(Community community, SiteUser siteUser,
            CommentRequestDto commentRequestDto) {
        return Comment.builder()
                .id(1L)
                .community(community)
                .siteUser(siteUser)
                .content(commentRequestDto.getContent())
                .postStatus(PostStatus.MODIFY)
                .createdAt(LocalDateTime.now())
                .build();
    }

    private static Comment getDeletedComment(Community community, SiteUser siteUser) {
        return Comment.builder()
                .id(1L)
                .community(community)
                .siteUser(siteUser)
                .content("delete Content")
                .postStatus(PostStatus.DELETE)
                .createdAt(LocalDateTime.now())
                .build();
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
