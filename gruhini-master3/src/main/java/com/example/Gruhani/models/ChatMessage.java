package com.example.Gruhani.models;

import javax.persistence.*;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

/**
 * ChatMessage - JPA Entity for storing chat messages in database
 * 
 * Used for:
 * - Persisting chat history
 * - Context retrieval for multi-turn conversations
 * - Analytics and Usage tracking
 * 
 * Database Table: chat_messages
 */
@Entity
@Table(name = "chat_messages", indexes = {
  @Index(name = "idx_conversation", columnList = "conversation_id"),
  @Index(name = "idx_user", columnList = "user_id"),
  @Index(name = "idx_created", columnList = "created_at")
})
public class ChatMessage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // Foreign key to conversations table (optional)
  @Column(name = "conversation_id", nullable = false)
  private String conversationId;

  // User who sent/received the message
  @Column(name = "user_id", nullable = false)
  private String userId;

  // Role: "user" or "bot"
  @Column(name = "role", nullable = false, length = 50)
  private String role;

  // Message content
  @Column(name = "content", columnDefinition = "LONGTEXT", nullable = false)
  private String content;

  // Number of tokens used in Gemini API call (for cost tracking)
  @Column(name = "tokens_used")
  private Integer tokensUsed;

  // Timestamp
  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  // ===== CONSTRUCTORS =====

  public ChatMessage() {}

  public ChatMessage(String conversationId, String userId, String role, String content) {
    this.conversationId = conversationId;
    this.userId = userId;
    this.role = role;
    this.content = content;
  }

  // ===== GETTERS & SETTERS =====

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getConversationId() {
    return conversationId;
  }

  public void setConversationId(String conversationId) {
    this.conversationId = conversationId;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Integer getTokensUsed() {
    return tokensUsed;
  }

  public void setTokensUsed(Integer tokensUsed) {
    this.tokensUsed = tokensUsed;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  @Override
  public String toString() {
    return "ChatMessage{" +
      "id=" + id +
      ", conversationId='" + conversationId + '\'' +
      ", userId='" + userId + '\'' +
      ", role='" + role + '\'' +
      ", content='" + content.substring(0, Math.min(50, content.length())) + "...'" +
      ", tokensUsed=" + tokensUsed +
      ", createdAt=" + createdAt +
      '}';
  }
}
