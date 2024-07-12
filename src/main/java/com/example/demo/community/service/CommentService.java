package com.example.demo.community.service;

import com.example.demo.community.dto.comment.CommentRequestDto;
import com.example.demo.community.entity.Comment;

public interface CommentService {

    Comment create(String email, Long communityId, CommentRequestDto commentRequestDto);

    Comment edit(String email, Long commentId, CommentRequestDto commentRequestDto);

    Comment delete(String email, Long commentId);
}
