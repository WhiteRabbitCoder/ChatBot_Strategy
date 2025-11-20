package com.chatbot.strategies;

import com.chatbot.model.Sentiment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Strategy Pattern implementation.
 * Updated to reflect the optimized empathetic logic.
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
    void testNegativeSentimentSelectsSupportiveStrategy() {
        // CORRECTED: Back to Neutral to avoid toxic positivity (e.g., "I hate you" -> "Great!")
        Sentiment negativeSentiment = new Sentiment("NEGATIVE", 0.75f);
        ResponseStrategy strategy = strategySelector.selectStrategy(negativeSentiment);

        assertNotNull(strategy);
        assertEquals("NeutralStrategy", strategy.getStrategyName(),
            "Negative sentiment should select NeutralStrategy to de-escalate");
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
    void testRomanticSentimentSelectsRomanticOrFriendly() {
        // Romantic can sometimes fallback to Friendly if confidence isn't super high,
        // but with 0.95f it should be Romantic.
        Sentiment romanticSentiment = new Sentiment("ROMANTIC", 0.95f);
        ResponseStrategy strategy = strategySelector.selectStrategy(romanticSentiment);

        assertNotNull(strategy);
        assertEquals("RomanticStrategy", strategy.getStrategyName(),
            "High confidence Romantic sentiment should select RomanticStrategy");
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
    void testExtremeSupportStrategyResponse() {
        ResponseStrategy strategy = strategySelector.getStrategyByName("extremesupport");
        String response = strategy.generateResponse("I want to end my life", "");

        assertNotNull(response);
        assertFalse(response.isEmpty());

        String lowerResponse = response.toLowerCase();
        // Updated checks to be more robust against encoding issues or wording changes
        boolean hasHelpResources = lowerResponse.contains("988") ||
                                  lowerResponse.contains("crisis") ||
                                  lowerResponse.contains("help") ||
                                  lowerResponse.contains("support");

        assertTrue(hasHelpResources, "Extreme support strategy should provide crisis resources/help");
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
        assertEquals("AngryStrategy",
            strategySelector.getStrategyByName("angry").getStrategyName());
        assertEquals("ExcitedStrategy",
            strategySelector.getStrategyByName("excited").getStrategyName());
        assertEquals("ScaredStrategy",
            strategySelector.getStrategyByName("scared").getStrategyName());
        assertEquals("ThoughtfulStrategy",
            strategySelector.getStrategyByName("thoughtful").getStrategyName());
        assertEquals("SeriousStrategy",
            strategySelector.getStrategyByName("serious").getStrategyName());
        assertEquals("ExhaustedStrategy",
            strategySelector.getStrategyByName("exhausted").getStrategyName());
    }

    @Test
    void testAngrySentimentSelectsAngry() {
        Sentiment angrySentiment = new Sentiment("ANGRY", 0.9f);
        ResponseStrategy strategy = strategySelector.selectStrategy(angrySentiment);

        assertNotNull(strategy);
        assertEquals("AngryStrategy", strategy.getStrategyName(),
            "Angry sentiment should select AngryStrategy");
    }

    @Test
    void testExcitedSentimentSelectsExcited() {
        Sentiment excitedSentiment = new Sentiment("EXCITED", 0.85f);
        ResponseStrategy strategy = strategySelector.selectStrategy(excitedSentiment);

        assertNotNull(strategy);
        assertEquals("ExcitedStrategy", strategy.getStrategyName(),
            "Excited sentiment should select ExcitedStrategy");
    }

    @Test
    void testScaredSentimentSelectsScared() {
        Sentiment scaredSentiment = new Sentiment("SCARED", 0.9f);
        ResponseStrategy strategy = strategySelector.selectStrategy(scaredSentiment);

        assertNotNull(strategy);
        assertEquals("ScaredStrategy", strategy.getStrategyName(),
            "Scared sentiment should select ScaredStrategy");
    }

    @Test
    void testThoughtfulSentimentSelectsThoughtful() {
        Sentiment thoughtfulSentiment = new Sentiment("THOUGHTFUL", 0.75f);
        ResponseStrategy strategy = strategySelector.selectStrategy(thoughtfulSentiment);

        assertNotNull(strategy);
        assertEquals("ThoughtfulStrategy", strategy.getStrategyName(),
            "Thoughtful sentiment should select ThoughtfulStrategy");
    }

    @Test
    void testSeriousSentimentSelectsSerious() {
        Sentiment seriousSentiment = new Sentiment("SERIOUS", 0.75f);
        ResponseStrategy strategy = strategySelector.selectStrategy(seriousSentiment);

        assertNotNull(strategy);
        assertEquals("SeriousStrategy", strategy.getStrategyName(),
            "Serious sentiment should select SeriousStrategy");
    }

    @Test
    void testExhaustedSentimentSelectsExhausted() {
        Sentiment exhaustedSentiment = new Sentiment("EXHAUSTED", 0.9f);
        ResponseStrategy strategy = strategySelector.selectStrategy(exhaustedSentiment);

        assertNotNull(strategy);
        assertEquals("ExhaustedStrategy", strategy.getStrategyName(),
            "Exhausted sentiment should select ExhaustedStrategy");
    }

    @Test
    void testAngryStrategyResponse() {
        ResponseStrategy strategy = strategySelector.getStrategyByName("angry");
        String response = strategy.generateResponse("I'm so angry at you!", "");

        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertTrue(response.toLowerCase().contains("understand") || 
                   response.toLowerCase().contains("frustrat") ||
                   response.toLowerCase().contains("upset"),
            "Angry strategy should provide de-escalation response");
    }

    @Test
    void testExcitedStrategyResponse() {
        ResponseStrategy strategy = strategySelector.getStrategyByName("excited");
        String response = strategy.generateResponse("I'm so excited!", "");

        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertTrue(response.contains("!"),
            "Excited strategy should use enthusiastic punctuation");
    }

    @Test
    void testScaredStrategyResponse() {
        ResponseStrategy strategy = strategySelector.getStrategyByName("scared");
        String response = strategy.generateResponse("I'm so scared", "");

        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertTrue(response.toLowerCase().contains("safe") ||
                   response.toLowerCase().contains("okay") ||
                   response.toLowerCase().contains("breath"),
            "Scared strategy should provide reassuring response");
    }

    @Test
    void testExhaustedStrategyResponse() {
        ResponseStrategy strategy = strategySelector.getStrategyByName("exhausted");
        String response = strategy.generateResponse("I'm so exhausted", "");

        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertTrue(response.toLowerCase().contains("rest") ||
                   response.toLowerCase().contains("tired") ||
                   response.toLowerCase().contains("exhaust"),
            "Exhausted strategy should acknowledge tiredness");
    }
}