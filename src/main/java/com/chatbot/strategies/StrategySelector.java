package com.chatbot.strategies;

import com.chatbot.model.Sentiment;

/**
 * Selects the appropriate ResponseStrategy based on detected sentiment.
 * FIXED: Negative sentiment now maps to Neutral to avoid toxic positivity.
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

    public ResponseStrategy selectStrategy(Sentiment sentiment) {
        // 1. CRITICAL SAFETY LAYER
        if (sentiment.isCrisis()) {
            return extremeSupportStrategy;
        }

        // 2. SPECIFIC EMOTIONAL STATES
        if (sentiment.isRomantic()) {
            return sentiment.getConfidence() > 0.7f ? romanticStrategy : friendlyStrategy;
        }

        if (sentiment.isSad()) {
            return sadStrategy;
        }

        if (sentiment.isNostalgic()) {
            return nostalgicStrategy;
        }

        // 3. GENERAL SENTIMENT HANDLING

        // Positive: Be friendly or funny
        if (sentiment.isPositive() && sentiment.getConfidence() >= positiveThreshold) {
            return Math.random() > 0.4 ? friendlyStrategy : humorousStrategy;
        }

        // Negative: Be Professional/Neutral (CORRECTION HERE)
        else if (sentiment.isNegative() && sentiment.getConfidence() >= negativeThreshold) {
            // Before we used FriendlyStrategy, which caused "Wonderful!" responses to "I hate you".
            // NeutralStrategy provides a safe, de-escalating response to aggression.
            return neutralStrategy;
        }

        // 4. DEFAULT
        else {
            return neutralStrategy;
        }
    }

    public ResponseStrategy getStrategyByName(String strategyName) {
        if (strategyName == null) return neutralStrategy;

        return switch (strategyName.toLowerCase()) {
            case "friendly" -> friendlyStrategy;
            case "humorous", "funny" -> humorousStrategy;
            case "romantic", "love" -> romanticStrategy;
            case "sad", "crying" -> sadStrategy;
            case "nostalgic", "memory" -> nostalgicStrategy;
            case "extremesupport", "crisis", "support", "help" -> extremeSupportStrategy;
            default -> neutralStrategy;
        };
    }
}