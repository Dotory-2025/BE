package com.dotoryteam.dotory.domain.chat.document;

import com.dotoryteam.dotory.domain.chat.entity.ChatRoomType;
import com.dotoryteam.dotory.domain.chat.entity.MessageType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@Document(collection = "chat_rooms")
@CompoundIndex(
        name = "idx_rooms_participants_last",
        def = "{'participantMemberIds': 1, 'lastMessage.sentAt': -1}"
)
public class ChatRoomDocument {

    @Id
    private String id;

    @Indexed(unique = true)
    private String roomKey;

    private ChatRoomType type; // DM, GROUP

    private Long houseId; // DM이면 null

    private List<Long> participantMemberIds;

    private LastMessage lastMessage;

    private Instant createdAt;

    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor @Builder
    public static class LastMessage {
        private String content;
        private MessageType type; // enum 적용
        private Long senderMemberId;
        private Instant sentAt;
    }
}
