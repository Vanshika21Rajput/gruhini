-- =====================================================
-- Gruhini AI Chatbot - Database Schema
-- =====================================================
-- Purpose: Store chat messages and conversation metadata
-- Run this on your database before starting the chatbot
-- =====================================================

USE gruhini;

-- ===== CHAT MESSAGES TABLE =====
-- Stores all messages (both user and bot)
CREATE TABLE IF NOT EXISTS chat_messages (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  
  -- Conversation identifier (groups messages into conversations)
  conversation_id VARCHAR(255) NOT NULL COMMENT 'Unique conversation ID (UUID)',
  
  -- User identifier (which customer sent this message)
  user_id VARCHAR(255) NOT NULL COMMENT 'User ID or email',
  
  -- Message role (either "user" or "bot")
  role VARCHAR(50) NOT NULL COMMENT '"user" for customer messages, "bot" for AI responses',
  
  -- Message content (can be very long)
  content LONGTEXT NOT NULL COMMENT 'Full message text',
  
  -- Tracking for cost/optimization
  tokens_used INT DEFAULT 0 COMMENT 'Number of tokens used in Gemini API call',
  
  -- Timestamp when message was created
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'When message was created',
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update time',
  
  -- Foreign key constraints
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  
  -- Indexes for performance
  INDEX idx_conversation (conversation_id) COMMENT 'Quick lookup by conversation',
  INDEX idx_user (user_id) COMMENT 'Quick lookup by user',
  INDEX idx_created (created_at) COMMENT 'For time-based queries',
  INDEX idx_role (role) COMMENT 'Filter by user/bot messages',
  
  -- Composite index for common queries
  INDEX idx_user_created (user_id, created_at) COMMENT 'Recent messages from user'
  
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_unicode_ci
COMMENT='Stores AI chatbot conversation messages';

-- ===== CHAT SESSIONS TABLE =====
-- Metadata about conversations
CREATE TABLE IF NOT EXISTS chat_sessions (
  id VARCHAR(255) PRIMARY KEY COMMENT 'Conversation UUID',
  
  -- Which user this conversation belongs to
  user_id VARCHAR(255) NOT NULL COMMENT 'User ID',
  
  -- Conversation title (auto-generated or user-set)
  title VARCHAR(255) DEFAULT NULL COMMENT 'Conversation title/topic',
  
  -- Statistics
  message_count INT DEFAULT 0 COMMENT 'Number of messages in conversation',
  token_usage INT DEFAULT 0 COMMENT 'Total tokens used (for cost tracking)',
  
  -- Time tracking
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'When conversation started',
  last_message_at TIMESTAMP NULL DEFAULT NULL COMMENT 'When last message sent',
  
  -- Foreign key
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  
  -- Indexes
  INDEX idx_user (user_id) COMMENT 'All conversations for a user',
  INDEX idx_created (created_at) COMMENT 'Sort by date'
  
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_unicode_ci
COMMENT='Chat conversation metadata and statistics';

-- ===== CHAT PREFERENCES TABLE =====
-- User preferences for chatbot behavior
CREATE TABLE IF NOT EXISTS chat_preferences (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  
  -- User this preference belongs to
  user_id VARCHAR(255) NOT NULL UNIQUE COMMENT 'User ID (one-to-one)',
  
  -- Language preference
  language VARCHAR(50) DEFAULT 'hi' COMMENT '"en", "hi", or "hinglish"',
  
  -- Settings
  notifications_enabled BOOLEAN DEFAULT true COMMENT 'Receive order update notifications',
  auto_suggest_on BOOLEAN DEFAULT true COMMENT 'Auto-suggest recommendations',
  theme VARCHAR(50) DEFAULT 'dark' COMMENT '"dark" or "light"',
  
  -- Dietary preferences
  dietary_restrictions VARCHAR(255) DEFAULT NULL COMMENT 'e.g., "vegetarian,no-salt"',
  preferred_cuisines VARCHAR(255) DEFAULT NULL COMMENT 'e.g., "north-indian,south-indian"',
  spice_level VARCHAR(50) DEFAULT 'medium' COMMENT '"mild", "medium", "spicy"',
  
  -- Budget preference
  budget_min INT DEFAULT 200 COMMENT 'Minimum budget in rupees',
  budget_max INT DEFAULT 1000 COMMENT 'Maximum budget in rupees',
  
  -- Timestamps
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'When preference created',
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last updated',
  
  -- Foreign key
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  
  -- Indexes
  INDEX idx_user (user_id) COMMENT 'Lookup by user'
  
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_unicode_ci
COMMENT='User preferences for chatbot personalization';

-- ===== CHAT ANALYTICS TABLE =====
-- Track usage patterns for analytics
CREATE TABLE IF NOT EXISTS chat_analytics (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  
  -- Time period
  day DATE NOT NULL COMMENT 'Date for daily stats',
  
  -- Counts
  unique_users INT DEFAULT 0 COMMENT 'Number of unique users who chatted',
  total_messages INT DEFAULT 0 COMMENT 'Total messages that day',
  total_conversations INT DEFAULT 0 COMMENT 'New conversations started',
  avg_response_time_ms INT DEFAULT 0 COMMENT 'Average API response time',
  
  -- API costs
  tokens_used INT DEFAULT 0 COMMENT 'Total tokens used',
  estimated_cost_rupees DECIMAL(10, 2) DEFAULT 0 COMMENT 'Estimated cost in rupees',
  
  -- Quality metrics
  user_satisfaction_score DECIMAL(3, 2) DEFAULT 0 COMMENT 'Avg user rating (1-5)',
  error_rate_percent DECIMAL(5, 2) DEFAULT 0 COMMENT 'Percentage of failed requests',
  
  -- Timestamps
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  
  -- Unique constraint
  UNIQUE KEY uk_day (day) COMMENT 'One record per day',
  
  -- Indexes
  INDEX idx_created (created_at) COMMENT 'Recent analytics'
  
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_unicode_ci
COMMENT='Daily analytics for chatbot usage and costs';

-- ===== CREATE VIEWS FOR EASY QUERYING =====

-- View: Recent conversations
CREATE OR REPLACE VIEW v_recent_conversations AS
SELECT 
  cs.id as conversation_id,
  cs.user_id,
  cs.title,
  cs.message_count,
  cs.token_usage,
  cs.created_at,
  cs.last_message_at,
  COUNT(cm.id) as total_messages
FROM chat_sessions cs
LEFT JOIN chat_messages cm ON cs.id = cm.conversation_id
GROUP BY cs.id
ORDER BY cs.last_message_at DESC;

-- View: User chat statistics
CREATE OR REPLACE VIEW v_user_chat_stats AS
SELECT 
  u.id as user_id,
  u.email,
  COUNT(DISTINCT cm.conversation_id) as total_conversations,
  COUNT(cm.id) as total_messages,
  SUM(CASE WHEN cm.role = 'user' THEN 1 ELSE 0 END) as user_messages,
  SUM(CASE WHEN cm.role = 'bot' THEN 1 ELSE 0 END) as bot_responses,
  MAX(cm.created_at) as last_chat_at
FROM users u
LEFT JOIN chat_messages cm ON u.id = cm.user_id
GROUP BY u.id;

-- ===== SAMPLE DATA (for testing) =====

-- This conversation shows the expected format
INSERT INTO chat_sessions (id, user_id, title, message_count) VALUES
('conv_sample_1', '1', 'Biryani Recommendation', 5);

INSERT INTO chat_messages (conversation_id, user_id, role, content, tokens_used) VALUES
('conv_sample_1', '1', 'user', 'What biryani do you recommend?', 12),
('conv_sample_1', '1', 'bot', '🍛 Based on your vegetarian preference, I recommend Chef Manju''s Mixed Vegetable Biryani (₹400). It has 4.9★ rating!', 85),
('conv_sample_1', '1', 'user', 'How long does delivery take?', 8),
('conv_sample_1', '1', 'bot', '🚚 Delivery typically takes 25-30 minutes to your area. Chef Manju is usually 20 mins.', 72),
('conv_sample_1', '1', 'user', 'Okay, I''ll order!', 5);

-- ===== STORED PROCEDURES FOR MAINTENANCE =====

-- Procedure: Clean up old messages (keep 90 days)
DELIMITER //
CREATE PROCEDURE sp_cleanup_old_chats()
BEGIN
  -- Delete old messages
  DELETE FROM chat_messages 
  WHERE created_at < DATE_SUB(NOW(), INTERVAL 90 DAY);
  
  -- Delete abandoned sessions
  DELETE FROM chat_sessions 
  WHERE created_at < DATE_SUB(NOW(), INTERVAL 90 DAY) 
  AND message_count = 0;
  
  SELECT 'Cleanup complete' AS status;
END //
DELIMITER ;

-- Procedure: Get conversation stats for a user
DELIMITER //
CREATE PROCEDURE sp_get_user_stats(IN p_user_id VARCHAR(255))
BEGIN
  SELECT 
    COUNT(DISTINCT conversation_id) as total_conversations,
    COUNT(*) as total_messages,
    SUM(tokens_used) as total_tokens_used,
    MAX(created_at) as last_message_date,
    ROUND(SUM(tokens_used) * 0.000000075, 2) as estimated_cost_rupees
  FROM chat_messages
  WHERE user_id = p_user_id;
END //
DELIMITER ;

-- ===== VERIFY INSTALLATION =====

-- Check if tables were created
SELECT 
  TABLE_NAME, 
  TABLE_TYPE, 
  TABLE_ROWS,
  CREATION_TIME
FROM INFORMATION_SCHEMA.TABLES 
WHERE TABLE_SCHEMA = 'gruhini' 
AND TABLE_NAME LIKE 'chat%'
ORDER BY CREATION_TIME DESC;

-- Show table structure
DESCRIBE chat_messages;
DESCRIBE chat_sessions;
DESCRIBE chat_preferences;

-- ===== OPTIMIZATION =====

-- Analyze tables for query optimization
ANALYZE TABLE chat_messages;
ANALYZE TABLE chat_sessions;
ANALYZE TABLE chat_preferences;

-- ===== BACKUP RECOMMENDATION =====

-- Before importing, backup existing database:
-- mysqldump -u root -p gruhini > gruhini_backup_$(date +%Y%m%d).sql

-- =====================================================
-- Installation Complete! ✅
-- You can now use the chatbot with database persistence
-- =====================================================

-- Verify:
-- SELECT * FROM chat_messages;
-- SELECT * FROM chat_sessions;
-- SELECT * FROM chat_preferences;
