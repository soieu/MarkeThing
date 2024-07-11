package com.example.demo.community.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import com.example.demo.community.dto.comment.CommentRequestDto;
import com.example.demo.community.entity.Comment;
import com.example.demo.community.entity.Community;
import com.example.demo.community.repository.CommentRepository;
import com.example.demo.community.repository.CommunityRepository;
import com.example.demo.community.service.impl.CommentServiceImpl;
import com.example.demo.community.type.AreaType;
import com.example.demo.exception.MarkethingException;
import com.example.demo.exception.type.ErrorCode;
import com.example.demo.siteuser.entity.SiteUser;
import com.example.demo.siteuser.repository.SiteUserRepository;
import com.example.demo.type.AuthType;
import com.example.demo.type.PostStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTest {

    @Mock
    private CommunityRepository communityRepository;

    @Mock
    private SiteUserRepository siteUserRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    void createSuccess() {
        // given
        SiteUser siteUser = getSiteUser();
        Community community = getCommunity();
        CommentRequestDto commentRequestDto = getCommentRequestDto();
        Comment comment = getComment(community, siteUser, commentRequestDto);

        given(siteUserRepository.findByEmail(siteUser.getEmail()))
                .willReturn(Optional.of(siteUser));
        given(communityRepository.findById(community.getId()))
                .willReturn(Optional.of(community));
        given(commentRepository.save(any(Comment.class)))
                .willReturn(comment);

        // when
        Comment result = commentService.create(siteUser.getEmail(),
                community.getId(), commentRequestDto);

        // then
        assertThat(result.getContent()).isEqualTo(commentRequestDto.getContent());
    }

    @Test
    void createFailedByUserNotFound() {
        // given
        given(siteUserRepository.findByEmail("mockEmail@gmail.com"))
                .willReturn(Optional.empty());

        // when
        MarkethingException exception = assertThrows(MarkethingException.class,
                () -> commentService.create("mockEmail@gmail.com",
                        1L, getCommentRequestDto()));
        // then
        assertEquals(exception.getErrorCode(), ErrorCode.USER_NOT_FOUND);
    }

    @Test
    void createFailedByCommunityNotFound() {
        // given
        SiteUser siteUser = getSiteUser();

        given(siteUserRepository.findByEmail(siteUser.getEmail()))
                .willReturn(Optional.of(siteUser));
        given(communityRepository.findById(eq(1L)))
                .willReturn(Optional.empty());

        // when
        MarkethingException exception = assertThrows(MarkethingException.class,
                () -> commentService.create("mockEmail@gmail.com",
                        1L, getCommentRequestDto()));
        // then
        assertEquals(exception.getErrorCode(), ErrorCode.COMMUNITY_NOT_FOUND);
    }

    @Test
    void editSuccess() {
        // given
        SiteUser siteUser = getSiteUser();
        Community community = getCommunity();
        CommentRequestDto commentRequestDto = getCommentRequestDto();
        CommentRequestDto editedcommentRequestDto = getEditCommentRequestDto();
        Comment comment = getComment(community, siteUser, commentRequestDto);

        given(commentRepository.findById(community.getId()))
                .willReturn(Optional.of(comment));

        // when
        Comment result = commentService.edit(siteUser.getEmail(), comment.getId(), editedcommentRequestDto);

        // then
        assertThat(result.getContent()).isEqualTo(editedcommentRequestDto.getContent());
    }

    @Test
    void editFailedByCommentNotFound() {
        // given
        given(commentRepository.findById(1L))
                .willReturn(Optional.empty());

        // when
        MarkethingException exception = assertThrows(MarkethingException.class,
                () -> commentService
                        .edit("mockEmail@gmail.com", 1L, getCommentRequestDto()));

        // then
        assertEquals(exception.getErrorCode(), ErrorCode.COMMENT_NOT_FOUND);
    }

    private static CommentRequestDto getCommentRequestDto() {
        return CommentRequestDto
                .builder()
                .content("content")
                .build();
    }

    private static CommentRequestDto getEditCommentRequestDto() {
        return CommentRequestDto
                .builder()
                .content("content")
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
