package com.example.demo.community.dto.comment;

import com.example.demo.community.entity.Comment;
import com.example.demo.community.entity.ReplyComment;
import com.example.demo.siteuser.entity.SiteUser;
import com.example.demo.type.PostStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReplyCommentRequestDto {
    private String content;

    public ReplyComment toEntity(SiteUser siteUser, Comment comment) {
        return ReplyComment.builder()
                .siteUser(siteUser)
                .comment(comment)
                .postStatus(PostStatus.POST)
                .content(content)
                .build();
    }
}
