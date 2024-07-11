package com.example.demo.community.service.impl;

import com.example.demo.community.dto.comment.CommentRequestDto;
import com.example.demo.community.entity.Comment;
import com.example.demo.community.repository.CommentRepository;
import com.example.demo.community.repository.CommunityRepository;
import com.example.demo.community.service.CommentService;
import com.example.demo.exception.MarkethingException;
import com.example.demo.exception.type.ErrorCode;
import com.example.demo.siteuser.repository.SiteUserRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final SiteUserRepository siteUserRepository;
    private final CommunityRepository communityRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public Comment create(String email, Long communityId, CommentRequestDto commentRequestDto) {
        var siteUser = siteUserRepository.findByEmail(email)
                .orElseThrow(() -> new MarkethingException(ErrorCode.USER_NOT_FOUND));
        var community = communityRepository.findById(communityId)
                .orElseThrow(() -> new MarkethingException(ErrorCode.COMMUNITY_NOT_FOUND));

        return commentRepository.save(commentRequestDto.toEntity(siteUser, community));
    }

    @Override
    @Transactional
    public Comment edit(String email, Long commentId, CommentRequestDto commentRequestDto) {
        var comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new MarkethingException(ErrorCode.COMMENT_NOT_FOUND));

        validateAuthorization(email, comment);

        comment.update(commentRequestDto);
        return comment;
    }

    private static void validateAuthorization(String email, Comment comment) {
        if(!email.equals(comment.getSiteUser().getEmail())) {
            throw new MarkethingException(ErrorCode.UNAUTHORIZED_USER);
        }
    }
}
