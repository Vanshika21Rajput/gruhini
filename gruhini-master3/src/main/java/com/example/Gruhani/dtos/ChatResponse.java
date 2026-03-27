package com.example.Gruhani.dtos;

import java.util.*;

/**
 * ChatResponse - DTO for chatbot responses
 */
public class ChatResponse {
  private boolean success;
  private String conversationId;
  private String response;
  private List<String> suggestions;
  private Date timestamp;
  private Integer tokensUsed;
  private Map<String, Object> metadata;

  // Constructors
  public ChatResponse() {
    this.success = true;
    this.timestamp = new Date();
    this.suggestions = new ArrayList<>();
    this.metadata = new HashMap<>();
  }

  public ChatResponse(boolean success, String conversationId, String response) {
    this();
    this.success = success;
    this.conversationId = conversationId;
    this.response = response;
  }

  // Getters & Setters
  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public String getConversationId() {
    return conversationId;
  }

  public void setConversationId(String conversationId) {
    this.conversationId = conversationId;
  }

  public String getResponse() {
    return response;
  }

  public void setResponse(String response) {
    this.response = response;
  }

  public List<String> getSuggestions() {
    return suggestions;
  }

  public void setSuggestions(List<String> suggestions) {
    this.suggestions = suggestions;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }

  public Integer getTokensUsed() {
    return tokensUsed;
  }

  public void setTokensUsed(Integer tokensUsed) {
    this.tokensUsed = tokensUsed;
  }

  public Map<String, Object> getMetadata() {
    return metadata;
  }

  public void setMetadata(Map<String, Object> metadata) {
    this.metadata = metadata;
  }

  @Override
  public String toString() {
    return "ChatResponse{" +
      "success=" + success +
      ", conversationId='" + conversationId + '\'' +
      ", response='" + response + '\'' +
      ", suggestions=" + suggestions +
      ", timestamp=" + timestamp +
      ", tokensUsed=" + tokensUsed +
      '}';
  }
}
