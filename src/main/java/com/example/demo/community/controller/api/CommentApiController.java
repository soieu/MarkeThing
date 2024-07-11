package com.example.demo.community.controller.api;

import com.example.demo.community.dto.comment.CommentRequestDto;
import com.example.demo.community.dto.comment.ReplyCommentRequestDto;
import com.example.demo.community.service.CommentService;
import com.example.demo.community.service.ReplyCommentService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/communities")
public class CommentApiController {
    private final CommentService commentService;
    private final ReplyCommentService replyCommentService;

    @PostMapping("{communityId}/comments")
    public void postComment(@RequestBody CommentRequestDto commentRequestDto
            , @PathVariable Long communityId, Principal principal) {

        var email = principal.getName();
        commentService.create(email, communityId, commentRequestDto);
    }

    @PatchMapping("/comments/{commentId}")
    public void editComment(@RequestBody CommentRequestDto commentRequestDto
            , @PathVariable Long commentId, Principal principal) {

        var email = principal.getName();
        commentService.edit(email, commentId, commentRequestDto);
    }

    @DeleteMapping("/comments/{commentId}")
    public void deleteComment(@PathVariable Long commentId, Principal principal) {

        var email = principal.getName();
        commentService.delete(email, commentId);
    }

    @PostMapping("/comments/{commentId}/replyComments")
    public void postReplyComment(@RequestBody ReplyCommentRequestDto replyCommentRequestDto
            , @PathVariable Long commentId, Principal principal) {

        var email = principal.getName();
        replyCommentService.create(email, commentId, replyCommentRequestDto);
    }

}
