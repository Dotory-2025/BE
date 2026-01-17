package com.dotoryteam.dotory.domain.chat.repository;

import com.dotoryteam.dotory.domain.chat.document.ChatRoomParticipantDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ChatRoomParticipantMongoRepository extends MongoRepository<ChatRoomParticipantDocument, String> {
    Optional<ChatRoomParticipantDocument> findByRoomKeyAndMemberId(String roomKey, Long memberId);
}
