package com.dotoryteam.dotory.domain.chat.document;

import com.dotoryteam.dotory.domain.chat.entity.MessageType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@Document(collection = "chat_messages")
@CompoundIndex(
        name = "idx_messages_room_sent",
        def = "{'roomKey': 1, 'sentAt': -1}"
)
public class ChatMessageDocument {

    @Id
    private String id;

    private String roomKey;

    private Long senderMemberId;

    private MessageType type; // TEXT, IMAGE, SYSTEM

    private String content;

    private Instant sentAt;
}
