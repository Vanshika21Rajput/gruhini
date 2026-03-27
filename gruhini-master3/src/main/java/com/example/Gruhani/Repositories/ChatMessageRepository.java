package com.example.Gruhani.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Gruhani.models.ChatMessage;

import java.util.*;

/**
 * ChatMessageRepository - JPA Repository for ChatMessage entity
 * 
 * Provides database operations for:
 * - Saving chat messages
 * - Retrieving conversation history
 * - Analytics queries
 */
@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

  /**
   * Find all messages in a conversation
   */
  List<ChatMessage> findByConversationId(String conversationId);

  /**
   * Find all messages from a user, ordered by date
   */
  List<ChatMessage> findByUserIdOrderByCreatedAtDesc(String userId);

  /**
   * Find recent messages for a conversation (for context)
   */
  @Query(value = "SELECT * FROM chat_messages WHERE conversation_id = :conversationId ORDER BY created_at DESC LIMIT :limit", nativeQuery = true)
  List<ChatMessage> findRecentByConversationId(@Param("conversationId") String conversationId, @Param("limit") int limit);

  /**
   * Find all user messages (for analytics)
   */
  @Query(value = "SELECT * FROM chat_messages WHERE user_id = :userId AND role = 'user' ORDER BY created_at DESC", nativeQuery = true)
  List<ChatMessage> findUserMessages(@Param("userId") String userId);

  /**
   * Count total messages from a user
   */
  Long countByUserId(String userId);

  /**
   * Count total conversations
   */
  @Query(value = "SELECT COUNT(DISTINCT conversation_id) FROM chat_messages", nativeQuery = true)
  Long countDistinctConversations();

  /**
   * Delete old messages (for cleanup)
   */
  @Query(value = "DELETE FROM chat_messages WHERE created_at < DATE_SUB(NOW(), INTERVAL 90 DAY)", nativeQuery = true)
  void deleteOldMessages();

  /**
   * Check if user has any messages
   */
  boolean existsByUserId(String userId);
}
