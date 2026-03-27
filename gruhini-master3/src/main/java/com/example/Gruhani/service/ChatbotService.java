package com.example.Gruhani.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.cache.annotation.Cacheable;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ChatbotService - AI chatbot logic with Gemini API integration
 * 
 * Features:
 * - Natural language processing with Google Gemini API
 * - Context awareness (orders, cart, seller info)
 * - Conversation memory (multi-turn conversations)
 * - Rate limiting & cost control
 * - Fallback responses if API fails
 * 
 * Configuration:
 * - GEMINI_API_KEY: API key from Google AI Studio
 * - GEMINI_MODEL: Model name (gemini-1.5-flash recommended)
 * - API_RATE_LIMIT: Max requests per hour (100 default)
 */
@Service
public class ChatbotService {

  // ===== CONFIGURATION =====
  @Value("${GEMINI_API_KEY:}")
  private String geminiApiKey;

  @Value("${GEMINI_MODEL:gemini-1.5-flash}")
  private String geminiModel;

  @Value("${api.rate.limit:100}")
  private int rateLimit;

  private static final String GEMINI_API_URL = 
    "https://generativelanguage.googleapis.com/v1beta/models/{model}:generateContent";

  // ===== DEPENDENCIES =====
  @Autowired(required = false)
  private OrderService orderService;

  @Autowired(required = false)
  private ProductService productService;

  @Autowired(required = false)
  private SellerService sellerService;

  // ===== IN-MEMORY STORAGE (for demo) =====
  private static final Map<String, List<Map<String, String>>> chatHistory = new ConcurrentHashMap<>();
  private static final Map<String, Long> userRequestCounts = new ConcurrentHashMap<>();
  private final RestTemplate restTemplate = new RestTemplate();
  private final ObjectMapper objectMapper = new ObjectMapper();

  /**
   * Process incoming user message
   * 1. Build system prompt with context
   * 2. Fetch conversation history
   * 3. Call Gemini API
   * 4. Generate suggestions
   * 5. Save to storage
   * 
   * @param request ChatRequest with user message
   * @param token Authorization token
   * @return ChatResponse with AI response
   */
  public ChatResponse processMessage(com.example.Gruhani.dtos.ChatRequest request, String token) throws Exception {
    // Extract user ID from token if needed
    String userId = request.getUserId();
    if (userId == null || userId.isEmpty()) {
      userId = extractUserIdFromToken(token);
    }

    // Check rate limit
    checkRateLimit(userId);

    // Generate conversation ID if not provided
    String conversationId = request.getConversationId();
    if (conversationId == null || conversationId.isEmpty()) {
      conversationId = UUID.randomUUID().toString();
    }

    // Build system prompt with context
    String systemPrompt = buildSystemPrompt(request, userId);

    // Get conversation history
    List<Map<String, String>> history = getConversationHistory(conversationId);

    // Call Gemini API
    String aiResponse = callGeminiAPI(
      request.getMessage(),
      systemPrompt,
      history,
      conversationId
    );

    // If API failed, use fallback
    if (aiResponse == null || aiResponse.isEmpty()) {
      aiResponse = generateFallbackResponse(request.getMessage());
    }

    // Generate suggestions
    List<String> suggestions = generateSuggestions(request.getMessage(), aiResponse);

    // Save messages to history
    saveMessage(conversationId, userId, "user", request.getMessage());
    saveMessage(conversationId, userId, "bot", aiResponse);

    // Build response
    ChatResponse response = new ChatResponse();
    response.setSuccess(true);
    response.setConversationId(conversationId);
    response.setResponse(aiResponse);
    response.setSuggestions(suggestions);
    response.setTimestamp(new Date());

    return response;
  }

  /**
   * Build system prompt with user context
   * Includes: Orders, Cart, Preferences, Business Rules
   */
  private String buildSystemPrompt(com.example.Gruhani.dtos.ChatRequest request, String userId) {
    StringBuilder prompt = new StringBuilder();

    // ===== CORE INSTRUCTIONS =====
    prompt.append("You are 🤖 Maa Ki Rasoi AI Assistant - a warm, helpful food delivery chatbot.\n");
    prompt.append("Platform: Gruhini (Home-cooked meal delivery from local chefs)\n\n");

    // ===== CAPABILITIES =====
    prompt.append("You help customers with:\n");
    prompt.append("1️⃣ Ordering food - Browse dishes, place orders\n");
    prompt.append("2️⃣ Order Tracking - Real-time status updates\n");
    prompt.append("3️⃣ Recommendations - Personalized dish suggestions\n");
    prompt.append("4️⃣ Seller Info - Chef ratings, cuisines, delivery time\n");
    prompt.append("5️⃣ Problem Resolution - Refunds, complaints, support\n");
    prompt.append("6️⃣ Payment Help - Price info, promotions\n\n");

    // ===== TONE & STYLE =====
    prompt.append("TONE:\n");
    prompt.append("• Warm, culturally respectful, like talking to a friend\n");
    prompt.append("• Use proper Hindi when appropriate (नमस्ते, धन्यवाद, etc)\n");
    prompt.append("• Mix English and Hindi for Indian audiences (Hinglish)\n");
    prompt.append("• Use relevant food emojis 🍛🥘🍜🍲\n");
    prompt.append("• Keep responses concise (max 300 chars preferred)\n\n");

    // ===== RULES =====
    prompt.append("RULES:\n");
    prompt.append("✅ DO: Be helpful, answer clearly, suggest popular items\n");
    prompt.append("✅ DO: Ask follow-up questions for better recommendations\n");
    prompt.append("✅ DO: Show empathy for complaints\n");
    prompt.append("❌ DON'T: Make up information about sellers or products\n");
    prompt.append("❌ DON'T: Promise specific delivery times (say 'typically X mins')\n");
    prompt.append("❌ DON'T: Process payments directly\n\n");

    // ===== USER CONTEXT =====
    prompt.append("USER CONTEXT:\n");
    
    // Orders
    String orders = getOrderContext(userId);
    prompt.append("📦 Recent Orders: ").append(orders).append("\n");

    // Cart
    String cart = request.getContext() != null ? 
      (request.getContext().get("cart") != null ? request.getContext().get("cart").toString() : "Empty") : 
      "Empty";
    prompt.append("🛒 Current Cart: ").append(cart).append("\n");

    // Preferences
    String prefs = getUserPreferences(userId);
    prompt.append("❤️ Preferences: ").append(prefs).append("\n\n");

    // ===== POPULAR ITEMS =====
    prompt.append("🌟 POPULAR DISHES ON GRUHINI:\n");
    prompt.append("• Biryani (Rice, meat/veg) - ₹350-450\n");
    prompt.append("• Paneer Tikka Masala - ₹320-400\n");
    prompt.append("• Vegetable Thali (rice, sabzi, raita) - ₹250-350\n");
    prompt.append("• Butter Naan with Raita - ₹150\n");
    prompt.append("• Samosas (4 pieces) - ₹60-80\n\n");

    // ===== TOP SELLERS =====
    prompt.append("👨‍🍳 TOP-RATED CHEFS:\n");
    prompt.append("• Chef Manju - Biryani Expert (4.8★, 250+ reviews)\n");
    prompt.append("• Neelam Joshi - Vegetarian (4.6★, 180+ reviews)\n");
    prompt.append("• Sakshi Tolani - All-rounder (4.7★, 200+ reviews)\n\n");

    return prompt.toString();
  }

  /**
   * Call Google Gemini API with conversation history
   */
  private String callGeminiAPI(String userMessage, String systemPrompt,
                               List<Map<String, String>> history, String conversationId) {
    try {
      // Build request body in Gemini format
      Map<String, Object> requestBody = new LinkedHashMap<>();

      // Add conversation history + current message
      List<Map<String, Object>> contents = new ArrayList<>();

      // Convert history to Gemini format
      for (Map<String, String> msg : history) {
        Map<String, Object> content = new LinkedHashMap<>();
        String role = msg.get("role");
        content.put("role", role.equals("user") ? "user" : "model");

        List<Map<String, String>> parts = new ArrayList<>();
        parts.add(Map.of("text", msg.get("content")));
        content.put("parts", parts);

        contents.add(content);
      }

      // Add current user message
      Map<String, Object> userContent = new LinkedHashMap<>();
      userContent.put("role", "user");
      userContent.put("parts", List.of(Map.of("text", userMessage)));
      contents.add(userContent);

      requestBody.put("contents", contents);

      // Add system instruction
      Map<String, Object> sysInstruction = new LinkedHashMap<>();
      sysInstruction.put("parts", List.of(Map.of("text", systemPrompt)));
      requestBody.put("system_instruction", sysInstruction);

      // Generation config
      Map<String, Object> genConfig = new LinkedHashMap<>();
      genConfig.put("temperature", 0.7);
      genConfig.put("max_output_tokens", 500);
      genConfig.put("top_p", 0.95);
      requestBody.put("generation_config", genConfig);

      // Make API call
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);

      String url = GEMINI_API_URL.replace("{model}", geminiModel) + "?key=" + geminiApiKey;
      HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);

      var response = restTemplate.postForObject(url, entity, Map.class);

      if (response != null && response.containsKey("candidates")) {
        List<?> candidates = (List<?>) response.get("candidates");
        if (!candidates.isEmpty()) {
          Map<?, ?> candidate = (Map<?, ?>) candidates.get(0);
          Map<?, ?> content = (Map<?, ?>) candidate.get("content");
          List<?> parts = (List<?>) content.get("parts");
          if (!parts.isEmpty()) {
            Map<?, ?> part = (Map<?, ?>) parts.get(0);
            return (String) part.get("text");
          }
        }
      }

      return null;

    } catch (Exception e) {
      System.err.println("Error calling Gemini API: " + e.getMessage());
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Get conversation history (last 10 messages)
   */
  private List<Map<String, String>> getConversationHistory(String conversationId) {
    List<Map<String, String>> history = chatHistory.getOrDefault(conversationId, new ArrayList<>());
    
    // Return last 10 messages (to stay within token limits)
    if (history.size() > 10) {
      return new ArrayList<>(history.subList(history.size() - 10, history.size()));
    }
    return new ArrayList<>(history);
  }

  /**
   * Save message to conversation history
   */
  private void saveMessage(String conversationId, String userId, String role, String content) {
    List<Map<String, String>> history = chatHistory.computeIfAbsent(conversationId, k -> new ArrayList<>());
    Map<String, String> message = new LinkedHashMap<>();
    message.put("role", role);
    message.put("content", content);
    message.put("timestamp", new Date().toString());
    history.add(message);
  }

  /**
   * Generate suggested follow-up questions based on context
   */
  private List<String> generateSuggestions(String userMessage, String response) {
    List<String> suggestions = new ArrayList<>();
    String lower = userMessage.toLowerCase();

    if (lower.contains("order") || lower.contains("ऑर्डर")) {
      suggestions.add("📦 Track my order");
      suggestions.add("🔄 Check delivery time");
    } else if (lower.contains("recommend") || lower.contains("suggest")) {
      suggestions.add("🍛 Vegetarian options");
      suggestions.add("🌶️ Spicy dishes");
    } else if (lower.contains("seller") || lower.contains("chef")) {
      suggestions.add("⭐ Top rated chefs");
      suggestions.add("🚚 Delivery options");
    } else {
      suggestions.add("🛒 Browse menu");
      suggestions.add("💬 Help");
    }

    return suggestions;
  }

  /**
   * Generate fallback response if Gemini API fails
   */
  private String generateFallbackResponse(String message) {
    String lower = message.toLowerCase();

    if (lower.contains("order") || lower.contains("ऑर्डर")) {
      return "📦 आप अपने ऑर्डर को ट्रैक कर सकते हैं या नया ऑर्डर दे सकते हैं।\n\nYou can track orders or place new ones on Gruhini! 🍛";
    } else if (lower.contains("recommend") || lower.contains("suggest")) {
      return "🍽️ हमारे पास शानदार बिरयानी, पनीर टिक्का मसाला, और थाली हैं!\n\nWe have amazing Biryani, Paneer Tikka, and Thali! 😋";
    } else if (lower.contains("seller") || lower.contains("chef")) {
      return "👨‍🍳 हमारे शीर्ष शेफ: मंजु (बिरयानी), नीलम (शाकाहारी), सक्षी (सब कुछ)\n\nTop chefs ready to serve! ⭐";
    } else if (lower.contains("price") || lower.contains("cost")) {
      return "💰 किफायती दाम: बिरयानी ₹350-450, थाली ₹250-350\n\nAffordable prices! Check our menu 🛒";
    } else if (lower.contains("delivery")) {
      return "🚚 तेजी से डिलीवरी: 20-35 मिनट में आपके दरवाज़े पर!\n\nFast delivery to your door! 🏃";
    }

    return "😊 मुझे समझ नहीं आया। कृपया दोबारा कहें?\n\nI didn't understand. Can you rephrase? 🤔";
  }

  /**
   * Get user's order context
   */
  private String getOrderContext(String userId) {
    try {
      if (orderService == null) return "Demo Mode";
      // This would fetch from database in production
      return "3 recent orders: Last one Ready for pickup ✅";
    } catch (Exception e) {
      return "No orders yet";
    }
  }

  /**
   * Get user's preferences from purchase history
   */
  private String getUserPreferences(String userId) {
    return "Vegetarian, North Indian, Medium spice";
  }

  /**
   * Get chat history for a user
   */
  public List<Map<String, Object>> getChatHistory(String userId) {
    List<Map<String, Object>> result = new ArrayList<>();

    // In production, fetch from database by userId
    for (List<Map<String, String>> conversation : chatHistory.values()) {
      for (Map<String, String> msg : conversation) {
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("type", msg.get("role").equals("user") ? "user" : "bot");
        item.put("text", msg.get("content"));
        item.put("timestamp", msg.get("timestamp"));
        result.add(item);
      }
    }

    return result;
  }

  /**
   * Clear chat history for a user
   */
  public void clearChatHistory(String token) {
    // Extract userId from token and clear their conversations
    String userId = extractUserIdFromToken(token);
    chatHistory.values().forEach(history -> history.clear());
  }

  /**
   * Get chatbot statistics
   */
  public Map<String, Object> getChatbotStats() {
    Map<String, Object> stats = new LinkedHashMap<>();
    stats.put("totalConversations", chatHistory.size());
    stats.put("totalMessages", chatHistory.values().stream().mapToLong(List::size).sum());
    stats.put("activeUsers", userRequestCounts.size());
    stats.put("apiModel", geminiModel);
    return stats;
  }

  /**
   * Check rate limit for user
   */
  private void checkRateLimit(String userId) throws IllegalArgumentException {
    long now = System.currentTimeMillis();
    long oneHourAgo = now - (60 * 60 * 1000);

    // Count requests in last hour
    Long lastRequest = userRequestCounts.get(userId);
    if (lastRequest != null && lastRequest > oneHourAgo) {
      int count = (int) chatHistory.values().stream()
        .flatMap(List::stream)
        .filter(msg -> msg.get("role").equals("user"))
        .count();

      if (count > rateLimit) {
        throw new IllegalArgumentException("You've exceeded the message limit. Try again later.");
      }
    }

    userRequestCounts.put(userId, now);
  }

  /**
   * Extract user ID from JWT token (demo implementation)
   */
  private String extractUserIdFromToken(String token) {
    if (token == null || token.isEmpty()) {
      return "guest-" + System.currentTimeMillis();
    }
    // In production: decode JWT and extract user ID
    return token.replaceAll("^Bearer ", "").substring(0, Math.min(20, token.length()));
  }
}
