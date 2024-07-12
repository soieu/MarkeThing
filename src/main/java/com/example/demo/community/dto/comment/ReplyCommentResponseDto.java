package com.example.demo.community.dto.comment;

import com.example.demo.community.entity.Comment;
import com.example.demo.community.entity.ReplyComment;
import com.example.demo.type.PostStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReplyCommentResponseDto {
    private long id;
    private long userId;
    private long commentId;
    private String content;
    private PostStatus postStatus;
    private LocalDateTime createdAt;

    public static ReplyCommentResponseDto fromEntity(ReplyComment replyComment) {
        return ReplyCommentResponseDto.builder()
                .id(replyComment.getId())
                .userId(replyComment.getSiteUser().getId())
                .commentId(replyComment.getId())
                .content(replyComment.getContent())
                .postStatus(replyComment.getPostStatus())
                .createdAt(replyComment.getCreatedAt())
                .build();
    }

}
