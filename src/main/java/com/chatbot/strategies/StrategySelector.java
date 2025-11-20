package com.chatbot.strategies;

import com.chatbot.model.Sentiment;

/**
 * Selects the appropriate ResponseStrategy based on detected sentiment.
 * This is the core of the Strategy pattern implementation.
 * Now includes support for romantic, sad, nostalgic, and crisis situations.
 */
public class StrategySelector {
    private final ResponseStrategy neutralStrategy;
    private final ResponseStrategy friendlyStrategy;
    private final ResponseStrategy humorousStrategy;
    private final ResponseStrategy romanticStrategy;
    private final ResponseStrategy sadStrategy;
    private final ResponseStrategy nostalgicStrategy;
    private final ResponseStrategy extremeSupportStrategy;
    
    private final float positiveThreshold = 0.6f;
    private final float negativeThreshold = 0.5f;
    
    public StrategySelector() {
        this.neutralStrategy = new NeutralStrategy();
        this.friendlyStrategy = new FriendlyStrategy();
        this.humorousStrategy = new HumorousStrategy();
        this.romanticStrategy = new RomanticStrategy();
        this.sadStrategy = new SadStrategy();
        this.nostalgicStrategy = new NostalgicStrategy();
        this.extremeSupportStrategy = new ExtremeSupportStrategy();
    }
    
    /**
     * Select the appropriate strategy based on detected sentiment.
     * Strategy selection logic (in priority order):
     * 1. CRISIS -> ExtremeSupportStrategy (highest priority for safety)
     * 2. ROMANTIC -> RomanticStrategy
     * 3. SAD -> SadStrategy
     * 4. NOSTALGIC -> NostalgicStrategy
     * 5. POSITIVE with high confidence -> FriendlyStrategy or HumorousStrategy
     * 6. NEGATIVE -> NeutralStrategy (to balance the mood)
     * 7. NEUTRAL or low confidence -> NeutralStrategy
     * 
     * @param sentiment The detected sentiment
     * @return The selected ResponseStrategy
     */
    public ResponseStrategy selectStrategy(Sentiment sentiment) {
        // HIGHEST PRIORITY: Crisis situations
        if (sentiment.isCrisis()) {
            return extremeSupportStrategy;
        }
        
        // SPECIFIC EMOTIONS: Romantic, Sad, Nostalgic
        if (sentiment.isRomantic()) {
            return romanticStrategy;
        }
        
        if (sentiment.isSad()) {
            return sadStrategy;
        }
        
        if (sentiment.isNostalgic()) {
            return nostalgicStrategy;
        }
        
        // GENERAL SENTIMENTS: Positive, Negative, Neutral
        if (sentiment.isPositive() && sentiment.getConfidence() >= positiveThreshold) {
            // For positive sentiment, alternate between friendly and humorous
            // to keep conversation dynamic
            return Math.random() > 0.5 ? friendlyStrategy : humorousStrategy;
        } else if (sentiment.isNegative() && sentiment.getConfidence() >= negativeThreshold) {
            // Use neutral strategy for negative sentiment to avoid escalation
            return neutralStrategy;
        } else {
            // Default to neutral for unclear or neutral sentiment
            return neutralStrategy;
        }
    }
    
    /**
     * Get a specific strategy by name (for testing or explicit selection).
     * 
     * @param strategyName The name of the strategy
     * @return The requested strategy, or neutral if not found
     */
    public ResponseStrategy getStrategyByName(String strategyName) {
        return switch (strategyName.toLowerCase()) {
            case "friendly" -> friendlyStrategy;
            case "humorous" -> humorousStrategy;
            case "neutral" -> neutralStrategy;
            case "romantic" -> romanticStrategy;
            case "sad" -> sadStrategy;
            case "nostalgic" -> nostalgicStrategy;
            case "extremesupport", "crisis" -> extremeSupportStrategy;
            default -> neutralStrategy;
        };
    }
}
