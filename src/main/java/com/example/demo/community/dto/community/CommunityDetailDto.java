package com.example.demo.community.dto.community;

import com.example.demo.community.dto.comment.CommentResponseDto;
import com.example.demo.community.entity.Community;
import com.example.demo.community.type.AreaType;
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
public class CommunityDetailDto {

    private long id;
    private AreaType area;
    private String title;
    private String content;
    private String postImg;
    private LocalDateTime createdAt;
    private List<CommentResponseDto> comments;

    public static CommunityDetailDto fromEntity(Community community) {
        return CommunityDetailDto.builder()
                .id(community.getId())
                .area(community.getArea())
                .title(community.getTitle())
                .content(community.getContent())
                .postImg(community.getPostImg())
                .createdAt(community.getCreatedAt())
                .comments(getCommentsDtoFromEntity(community))
                .build();
    }

    private static List<CommentResponseDto> getCommentsDtoFromEntity(Community community) {
        return Optional.ofNullable(community.getComments())
                .orElse(Collections.emptyList())
                .stream()
                .map(CommentResponseDto::fromEntity)
                .collect(Collectors.toList());
    }
}
