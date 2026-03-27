package com.example.Gruhani.dtos;

import java.util.*;

/**
 * ChatRequest - DTO for incoming chat messages
 */
public class ChatRequest {
  private String conversationId;
  private String userId;
  private String message;
  private Map<String, Object> context;

  // Constructors
  public ChatRequest() {}

  public ChatRequest(String conversationId, String userId, String message, Map<String, Object> context) {
    this.conversationId = conversationId;
    this.userId = userId;
    this.message = message;
    this.context = context;
  }

  // Getters & Setters
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

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Map<String, Object> getContext() {
    return context;
  }

  public void setContext(Map<String, Object> context) {
    this.context = context;
  }

  @Override
  public String toString() {
    return "ChatRequest{" +
      "conversationId='" + conversationId + '\'' +
      ", userId='" + userId + '\'' +
      ", message='" + message + '\'' +
      ", context=" + context +
      '}';
  }
}
