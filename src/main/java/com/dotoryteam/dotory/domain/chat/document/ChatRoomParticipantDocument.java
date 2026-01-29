package com.dotoryteam.dotory.domain.chat.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@Document(collection = "chat_room_participants")
@CompoundIndex(
        name = "uq_room_member",
        def = "{'roomKey': 1, 'memberId': 1}",
        unique = true
)
public class ChatRoomParticipantDocument {

    @Id
    private String id;

    private String roomKey;

    @Indexed
    private Long memberId;

    private Instant joinedAt;

    private Instant lastReadAt;
}
