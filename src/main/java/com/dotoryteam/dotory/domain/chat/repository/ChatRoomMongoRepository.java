package com.dotoryteam.dotory.domain.chat.repository;

import com.dotoryteam.dotory.domain.chat.document.ChatRoomDocument;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomMongoRepository extends MongoRepository<ChatRoomDocument, String> {
    Optional<ChatRoomDocument> findByRoomKey(String roomKey);

    List<ChatRoomDocument> findByParticipantMemberIdsContainsOrderByLastMessage_SentAtDesc(
            Long memberId, Pageable pageable
    );
}
