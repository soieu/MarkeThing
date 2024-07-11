package com.example.demo.community.service;

import com.example.demo.community.dto.comment.ReplyCommentRequestDto;
import com.example.demo.community.entity.ReplyComment;

public interface ReplyCommentService {

    ReplyComment create(String email, Long commentId, ReplyCommentRequestDto replyCommentRequestDto);

    ReplyComment edit(String email, Long replyId, ReplyCommentRequestDto commentRequestDto);

    ReplyComment delete(String email, Long replyId);
}
