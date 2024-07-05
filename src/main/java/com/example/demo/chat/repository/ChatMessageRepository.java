package com.example.demo.chat.repository;


import com.example.demo.chat.entiity.ChatMessage;
import javax.persistence.Id;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, Id> {

}
