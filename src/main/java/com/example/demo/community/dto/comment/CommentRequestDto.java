package com.example.demo.community.dto.comment;

import com.example.demo.community.entity.Comment;
import com.example.demo.community.entity.Community;
import com.example.demo.siteuser.entity.SiteUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestDto {
    private String content;

    public Comment toEntity(SiteUser siteUser, Community community) {
        return Comment.builder()
                .community(community)
                .siteUser(siteUser)
                .content(content)
                .build();
    }
}
