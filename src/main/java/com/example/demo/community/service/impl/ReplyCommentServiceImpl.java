package com.example.demo.community.service.impl;

import com.example.demo.community.dto.comment.CommentRequestDto;
import com.example.demo.community.dto.comment.ReplyCommentRequestDto;
import com.example.demo.community.entity.Comment;
import com.example.demo.community.entity.ReplyComment;
import com.example.demo.community.repository.CommentRepository;
import com.example.demo.community.repository.CommunityRepository;
import com.example.demo.community.repository.ReplyCommentRepository;
import com.example.demo.community.service.CommentService;
import com.example.demo.community.service.ReplyCommentService;
import com.example.demo.exception.MarkethingException;
import com.example.demo.exception.type.ErrorCode;
import com.example.demo.siteuser.repository.SiteUserRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReplyCommentServiceImpl implements ReplyCommentService {

    private final SiteUserRepository siteUserRepository;
    private final CommentRepository commentRepository;
    private final ReplyCommentRepository replyCommentRepository;

    @Override
    @Transactional
    public ReplyComment create(String email, Long commentId,
            ReplyCommentRequestDto replyCommentRequestDto) {
        var siteUser = siteUserRepository.findByEmail(email)
                .orElseThrow(() -> new MarkethingException(ErrorCode.USER_NOT_FOUND));
        var comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new MarkethingException(ErrorCode.COMMENT_NOT_FOUND));

        return replyCommentRepository.save(replyCommentRequestDto.toEntity(siteUser, comment));
    }
}
