package com.chatbot.strategies;

import com.chatbot.model.Sentiment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Strategy Pattern implementation.
 */
class StrategyPatternTest {
    
    private StrategySelector strategySelector;
    
    @BeforeEach
    void setUp() {
        strategySelector = new StrategySelector();
    }
    
    @Test
    void testPositiveSentimentSelectsFriendlyOrHumorous() {
        Sentiment positiveSentiment = new Sentiment("POSITIVE", 0.85f);
        ResponseStrategy strategy = strategySelector.selectStrategy(positiveSentiment);
        
        assertNotNull(strategy);
        assertTrue(
            strategy.getStrategyName().equals("FriendlyStrategy") || 
            strategy.getStrategyName().equals("HumorousStrategy"),
            "Positive sentiment should select FriendlyStrategy or HumorousStrategy"
        );
    }
    
    @Test
    void testNegativeSentimentSelectsNeutral() {
        Sentiment negativeSentiment = new Sentiment("NEGATIVE", 0.75f);
        ResponseStrategy strategy = strategySelector.selectStrategy(negativeSentiment);
        
        assertNotNull(strategy);
        assertEquals("NeutralStrategy", strategy.getStrategyName(),
            "Negative sentiment should select NeutralStrategy");
    }
    
    @Test
    void testNeutralSentimentSelectsNeutral() {
        Sentiment neutralSentiment = new Sentiment("NEUTRAL", 0.60f);
        ResponseStrategy strategy = strategySelector.selectStrategy(neutralSentiment);
        
        assertNotNull(strategy);
        assertEquals("NeutralStrategy", strategy.getStrategyName(),
            "Neutral sentiment should select NeutralStrategy");
    }
    
    @Test
    void testLowConfidencePositiveSelectsNeutral() {
        Sentiment lowConfidencePositive = new Sentiment("POSITIVE", 0.45f);
        ResponseStrategy strategy = strategySelector.selectStrategy(lowConfidencePositive);
        
        assertNotNull(strategy);
        assertEquals("NeutralStrategy", strategy.getStrategyName(),
            "Low confidence positive should select NeutralStrategy");
    }
    
    @Test
    void testNeutralStrategyResponse() {
        ResponseStrategy strategy = strategySelector.getStrategyByName("neutral");
        String response = strategy.generateResponse("Hello", "");
        
        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertTrue(response.toLowerCase().contains("hello") || 
                   response.toLowerCase().contains("assist"),
            "Neutral strategy should respond appropriately to greetings");
    }
    
    @Test
    void testFriendlyStrategyResponse() {
        ResponseStrategy strategy = strategySelector.getStrategyByName("friendly");
        String response = strategy.generateResponse("I'm having a great day!", "");
        
        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertTrue(response.contains("!"),
            "Friendly strategy should use enthusiastic punctuation");
    }
    
    @Test
    void testHumorousStrategyResponse() {
        ResponseStrategy strategy = strategySelector.getStrategyByName("humorous");
        String response = strategy.generateResponse("Tell me a joke", "");
        
        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertTrue(response.length() > 10,
            "Humorous strategy should provide substantive responses");
    }
    
    @Test
    void testStrategyWithConversationHistory() {
        ResponseStrategy strategy = strategySelector.getStrategyByName("neutral");
        String history = "User: Hello\nBot: Hi there";
        String response = strategy.generateResponse("How are you?", history);
        
        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertTrue(response.length() > 5,
            "Strategy should generate meaningful response with history");
    }
    
    @Test
    void testGetStrategyByNameReturnsCorrectStrategy() {
        assertEquals("NeutralStrategy", 
            strategySelector.getStrategyByName("neutral").getStrategyName());
        assertEquals("FriendlyStrategy", 
            strategySelector.getStrategyByName("friendly").getStrategyName());
        assertEquals("HumorousStrategy", 
            strategySelector.getStrategyByName("humorous").getStrategyName());
    }
    
    @Test
    void testGetStrategyByNameDefaultsToNeutral() {
        ResponseStrategy strategy = strategySelector.getStrategyByName("invalid");
        assertEquals("NeutralStrategy", strategy.getStrategyName(),
            "Invalid strategy name should default to NeutralStrategy");
    }
}
