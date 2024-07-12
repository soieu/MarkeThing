package com.example.demo.chat.repository;

import com.example.demo.chat.entiity.ChatRoom;
import com.example.demo.siteuser.entity.SiteUser;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {

    List<ChatRoom> findByAgentId(Long id); // 대행희망자

    List<ChatRoom> findByRequesterId(Long id); // 의뢰자





}
