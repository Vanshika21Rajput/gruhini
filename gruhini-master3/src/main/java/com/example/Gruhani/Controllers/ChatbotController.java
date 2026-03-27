package com.example.Gruhani.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.Gruhani.service.ChatbotService;
import com.example.Gruhani.dtos.ChatRequest;
import com.example.Gruhani.dtos.ChatResponse;

import java.util.*;

/**
 * ChatbotController - REST endpoints for AI chatbot
 * 
 * Endpoints:
 * - POST /api/chat - Send message and get AI response
 * - GET /api/chat-history/{userId} - Get chat history
 * - DELETE /api/chat-clear - Clear chat history
 * 
 * Integration: Gemini API for natural language processing
 * Context: Order status, product recommendations, seller info
 */
@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE})
public class ChatbotController {

  @Autowired
  private ChatbotService chatbotService;

  /**
   * POST /api/chat
   * 
   * Send a message to the chatbot and receive AI-powered response
   * with context awareness for orders, products, and seller info
   * 
   * @param request ChatRequest containing:
   *   - conversationId: Unique conversation identifier (nullable on first message)
   *   - userId: User identifier
   *   - message: User's text message
   *   - context: Cart and order context
   * @param token Authorization token from header
   * @return ChatResponse with AI response and suggestions
   * 
   * Example Request:
   * {
   *   "conversationId": "conv_123",
   *   "userId": "user_456",
   *   "message": "What dishes do you recommend?",
   *   "context": {
   *     "cart": ["Biryani x2", "Raita x1"],
   *     "orders": [...]
   *   }
   * }
   * 
   * Example Response:
   * {
   *   "success": true,
   *   "conversationId": "conv_123",
   *   "response": "🍽️ Based on your preferences, I recommend...",
   *   "suggestions": ["Track order", "View sellers"],
   *   "timestamp": "2026-03-24T18:30:00Z"
   * }
   */
  @PostMapping
  public ResponseEntity<?> sendMessage(
    @RequestBody ChatRequest request,
    @RequestHeader(value = "Authorization", required = false) String token) {
    
    try {
      // Validate input
      if (request == null || request.getMessage() == null || request.getMessage().trim().isEmpty()) {
        return ResponseEntity.badRequest().body(Map.of(
          "success", false,
          "error", "Message cannot be empty"
        ));
      }

      // Sanitize input (prevent prompt injection)
      String sanitized = sanitizeInput(request.getMessage());
      request.setMessage(sanitized);

      // Process message with chatbot service
      ChatResponse response = chatbotService.processMessage(request, token);

      return ResponseEntity.ok(response);

    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(Map.of(
        "success", false,
        "error", e.getMessage()
      ));
    } catch (Exception e) {
      System.err.println("Error in chatbot controller: " + e.getMessage());
      e.printStackTrace();
      return ResponseEntity.status(500).body(Map.of(
        "success", false,
        "error", "Internal server error. Please try again later."
      ));
    }
  }

  /**
   * GET /api/chat-history/{userId}
   * 
   * Retrieve chat history for a specific user
   * 
   * @param userId User identifier
   * @param token Authorization token
   * @return List of previous messages in conversation
   * 
   * Example Response:
   * {
   *   "success": true,
   *   "messages": [
   *     { "type": "user", "text": "What do you recommend?", "timestamp": "..." },
   *     { "type": "bot", "text": "Based on preferences...", "timestamp": "..." }
   *   ]
   * }
   */
  @GetMapping("/history/{userId}")
  public ResponseEntity<?> getChatHistory(
    @PathVariable String userId,
    @RequestHeader(value = "Authorization", required = false) String token) {
    
    try {
      if (userId == null || userId.trim().isEmpty()) {
        return ResponseEntity.badRequest().body(Map.of(
          "success", false,
          "error", "User ID is required"
        ));
      }

      List<Map<String, Object>> history = chatbotService.getChatHistory(userId);

      return ResponseEntity.ok(Map.of(
        "success", true,
        "messages", history
      ));

    } catch (Exception e) {
      return ResponseEntity.status(500).body(Map.of(
        "success", false,
        "error", "Failed to retrieve chat history"
      ));
    }
  }

  /**
   * DELETE /api/chat-clear
   * 
   * Clear all chat history for the current user
   * 
   * @param token Authorization token (extracts user ID)
   * @return Success confirmation
   * 
   * Example Response:
   * {
   *   "success": true,
   *   "message": "Chat history cleared successfully"
   * }
   */
  @DeleteMapping("/clear")
  public ResponseEntity<?> clearChat(
    @RequestHeader(value = "Authorization", required = false) String token) {
    
    try {
      if (token == null || token.trim().isEmpty()) {
        return ResponseEntity.status(401).body(Map.of(
          "success", false,
          "error", "Authorization required"
        ));
      }

      chatbotService.clearChatHistory(token);

      return ResponseEntity.ok(Map.of(
        "success", true,
        "message", "Chat history cleared successfully"
      ));

    } catch (Exception e) {
      return ResponseEntity.status(500).body(Map.of(
        "success", false,
        "error", "Failed to clear chat history"
      ));
    }
  }

  /**
   * GET /api/chat/stats
   * 
   * Get chatbot usage statistics (optional)
   * 
   * @return Statistics about chatbot usage
   */
  @GetMapping("/stats")
  public ResponseEntity<?> getChatbotStats() {
    try {
      Map<String, Object> stats = chatbotService.getChatbotStats();
      return ResponseEntity.ok(Map.of(
        "success", true,
        "stats", stats
      ));
    } catch (Exception e) {
      return ResponseEntity.status(500).body(Map.of(
        "success", false,
        "error", "Failed to retrieve stats"
      ));
    }
  }

  /**
   * Sanitize user input to prevent prompt injection attacks
   * Remove special characters that could manipulate AI responses
   */
  private String sanitizeInput(String input) {
    return input
      .replaceAll("[<>\"'{};\\[\\]]", "")  // Remove dangerous chars
      .replaceAll("(?i)(system|prompt|instruction|api_key|password)", "")  // Remove sensitive keywords
      .trim()
      .substring(0, Math.min(1000, input.length()));  // Limit length
  }
}
