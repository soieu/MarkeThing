package com.example.demo.auth.sms;

import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class MessageSender {

    @Value("${sms.api-key}")
    private String apiKey;
    @Value("${sms.api-secret}")
    private String apiSecret;
    @Value("${sms.provider}")
    private String provider;
    @Value("${sms.sender}")
    private String sender;

    public void send(String phoneNumber, String text) {
        DefaultMessageService messageService = new DefaultMessageService(apiKey, apiSecret,
                provider);
        Message message = new Message();
        message.setFrom(sender);
        message.setTo(phoneNumber);
        message.setText(text);
        messageService.sendOne(new SingleMessageSendingRequest(message));
    }
}
