package com.dotoryteam.dotory.domain.chat.repository;

import com.dotoryteam.dotory.domain.chat.document.ChatMessageDocument;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageMongoRepository extends MongoRepository<ChatMessageDocument, String> {
    List<ChatMessageDocument> findByRoomKeyOrderBySentAtDesc(String roomKey, Pageable pageable);
}
