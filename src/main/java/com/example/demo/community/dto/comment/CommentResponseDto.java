package com.example.demo.community.dto.comment;

import com.example.demo.community.entity.Comment;
import com.example.demo.type.PostStatus;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto {
    private long id;
    private long userId;
    private String content;
    private PostStatus postStatus;
    private LocalDateTime createdAt;
    private List<ReplyCommentResponseDto> replyComments;

    public static CommentResponseDto fromEntity(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .userId(comment.getSiteUser().getId())
                .content(comment.getContent())
                .postStatus(comment.getPostStatus())
                .createdAt(comment.getCreatedAt())
                .replyComments(getReplyCommentsDtoFromEntity(comment))
                .build();
    }

    private static List<ReplyCommentResponseDto> getReplyCommentsDtoFromEntity(Comment comment) {
        return Optional.ofNullable(comment.getReplyComments())
                .orElse(Collections.emptyList())
                .stream()
                .map(ReplyCommentResponseDto::fromEntity)
                .collect(Collectors.toList());
    }
}
