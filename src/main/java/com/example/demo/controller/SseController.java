package com.example.demo.controller;

import com.example.demo.service.SseEmitterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor

public class SseController {
    private final SseEmitterService sseEmitterService;


    @GetMapping(path = "/v1/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> subscribe(@RequestParam String userId) {//구독을 추가하고 이후 자신에게 온 알람을 확인할 수 있음.
        //저장된 user의 id로 향해 메시지가 전송이 됨.
        SseEmitter emitter = sseEmitterService.subscribe(userId);
        return ResponseEntity.ok(emitter);
    }

    @GetMapping(path = "/v1/subscribe/send")
    public ResponseEntity<SseEmitter> test(@RequestParam String userId) {//알람 이벤트를 발생시킴
        sseEmitterService.publish(userId);//알람 메시지를 전송을 하게함
        return ResponseEntity.ok().build();
    }
}
