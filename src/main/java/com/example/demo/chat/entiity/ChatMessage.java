package com.example.demo.chat.entiity;



import java.time.LocalDate;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "ChatMessage") // 실제 몽고 DB 컬렉션 이름
@EntityListeners(AuditingEntityListener.class)
public class ChatMessage {

    @Id
    private ObjectId id; // 몽고디비에서 id를 자동생성하기 위한 데이터 타잆
    private Long chatRoomId;
    private Long senderId;
    private String content;

    @CreatedDate // 생성 시간
    private LocalDate createdAt;
}
