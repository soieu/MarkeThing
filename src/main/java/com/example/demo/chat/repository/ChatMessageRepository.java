package com.example.demo.chat.repository;


import com.example.demo.chat.entiity.ChatMessage;
import com.example.demo.chat.entiity.ChatRoom;
import java.util.List;
import javax.persistence.Id;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, Id> {

    ChatMessage findFirstByChatRoomIdOrderByCreatedAtDesc(Long chatRoomId);

    void deleteByChatRoomId(Long chatRoomId);
    List<ChatMessage> findByChatRoomId(Long chatRoomId);

}
