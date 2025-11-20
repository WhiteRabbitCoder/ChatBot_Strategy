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
    
    @Test
    void testRomanticSentimentSelectsRomantic() {
        Sentiment romanticSentiment = new Sentiment("ROMANTIC", 0.80f);
        ResponseStrategy strategy = strategySelector.selectStrategy(romanticSentiment);
        
        assertNotNull(strategy);
        assertEquals("RomanticStrategy", strategy.getStrategyName(),
            "Romantic sentiment should select RomanticStrategy");
    }
    
    @Test
    void testSadSentimentSelectsSad() {
        Sentiment sadSentiment = new Sentiment("SAD", 0.85f);
        ResponseStrategy strategy = strategySelector.selectStrategy(sadSentiment);
        
        assertNotNull(strategy);
        assertEquals("SadStrategy", strategy.getStrategyName(),
            "Sad sentiment should select SadStrategy");
    }
    
    @Test
    void testNostalgicSentimentSelectsNostalgic() {
        Sentiment nostalgicSentiment = new Sentiment("NOSTALGIC", 0.75f);
        ResponseStrategy strategy = strategySelector.selectStrategy(nostalgicSentiment);
        
        assertNotNull(strategy);
        assertEquals("NostalgicStrategy", strategy.getStrategyName(),
            "Nostalgic sentiment should select NostalgicStrategy");
    }
    
    @Test
    void testCrisisSentimentSelectsExtremeSupport() {
        Sentiment crisisSentiment = new Sentiment("CRISIS", 0.99f);
        ResponseStrategy strategy = strategySelector.selectStrategy(crisisSentiment);
        
        assertNotNull(strategy);
        assertEquals("ExtremeSupportStrategy", strategy.getStrategyName(),
            "Crisis sentiment should select ExtremeSupportStrategy");
    }
    
    @Test
    void testRomanticStrategyResponse() {
        ResponseStrategy strategy = strategySelector.getStrategyByName("romantic");
        String response = strategy.generateResponse("I love you", "");
        
        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertTrue(response.toLowerCase().contains("love") || response.contains("❤") || response.contains("💕"),
            "Romantic strategy should respond with loving language");
    }
    
    @Test
    void testSadStrategyResponse() {
        ResponseStrategy strategy = strategySelector.getStrategyByName("sad");
        String response = strategy.generateResponse("I'm feeling sad", "");
        
        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertTrue(response.toLowerCase().contains("sorry") || 
                   response.toLowerCase().contains("understand") ||
                   response.toLowerCase().contains("difficult") ||
                   response.toLowerCase().contains("sad") ||
                   response.toLowerCase().contains("feel"),
            "Sad strategy should respond with empathetic language");
    }
    
    @Test
    void testNostalgicStrategyResponse() {
        ResponseStrategy strategy = strategySelector.getStrategyByName("nostalgic");
        String response = strategy.generateResponse("I remember when", "");
        
        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertTrue(response.toLowerCase().contains("memor") || response.toLowerCase().contains("past"),
            "Nostalgic strategy should respond with reflective language");
    }
    
    @Test
    void testExtremeSupportStrategyResponse() {
        ResponseStrategy strategy = strategySelector.getStrategyByName("extremesupport");
        String response = strategy.generateResponse("I want to end my life", "");
        
        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertTrue(response.contains("988") || response.contains("crisis") || response.contains("help"),
            "Extreme support strategy should provide crisis resources");
        assertTrue(response.contains("🚨") || response.contains("🆘"),
            "Extreme support strategy should use urgent indicators");
    }
    
    @Test
    void testAllNewStrategiesInSelector() {
        assertEquals("RomanticStrategy", 
            strategySelector.getStrategyByName("romantic").getStrategyName());
        assertEquals("SadStrategy", 
            strategySelector.getStrategyByName("sad").getStrategyName());
        assertEquals("NostalgicStrategy", 
            strategySelector.getStrategyByName("nostalgic").getStrategyName());
        assertEquals("ExtremeSupportStrategy", 
            strategySelector.getStrategyByName("crisis").getStrategyName());
    }
}
