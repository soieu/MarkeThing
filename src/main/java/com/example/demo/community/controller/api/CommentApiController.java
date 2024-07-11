package com.example.demo.community.controller.api;

import com.example.demo.community.dto.comment.CommentRequestDto;
import com.example.demo.community.service.CommentService;
import lombok.RequiredArgsConstructor;
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
    // 회원 가입 기능 구현 완료 후 user 정보 가져오기 위해 Principal 객체 request에 추가
    @PostMapping("{communityId}/comments")
    public void postComment(@RequestBody CommentRequestDto commentRequestDto
            , @PathVariable Long communityId) {

        String email = "mockEmail@gmail.com";
        commentService.create(email, communityId, commentRequestDto);
    }

    @PatchMapping("/comments/{commentId}")
    public void editComment(@RequestBody CommentRequestDto commentRequestDto
            , @PathVariable Long commentId) {

        String email = "mockEmail@gmail.com";
        commentService.edit(email, commentId, commentRequestDto);
    }



}
