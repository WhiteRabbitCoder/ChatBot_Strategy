package com.chatbot.strategies;

import com.chatbot.model.Sentiment;
import com.chatbot.model.StrategyMatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Selects the appropriate ResponseStrategy based on detected sentiment.
 * IMPROVED: Uses weighted scoring instead of "first match wins".
 */
public class StrategySelector {
    private final ResponseStrategy neutralStrategy;
    private final ResponseStrategy friendlyStrategy;
    private final ResponseStrategy humorousStrategy;
    private final ResponseStrategy romanticStrategy;
    private final ResponseStrategy sadStrategy;
    private final ResponseStrategy nostalgicStrategy;
    private final ResponseStrategy extremeSupportStrategy;
    private final ResponseStrategy angryStrategy;
    private final ResponseStrategy excitedStrategy;
    private final ResponseStrategy scaredStrategy;
    private final ResponseStrategy thoughtfulStrategy;
    private final ResponseStrategy seriousStrategy;
    private final ResponseStrategy exhaustedStrategy;

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
        this.angryStrategy = new AngryStrategy();
        this.excitedStrategy = new ExcitedStrategy();
        this.scaredStrategy = new ScaredStrategy();
        this.thoughtfulStrategy = new ThoughtfulStrategy();
        this.seriousStrategy = new SeriousStrategy();
        this.exhaustedStrategy = new ExhaustedStrategy();
    }

    public ResponseStrategy selectStrategy(Sentiment sentiment) {
        // 1. CRITICAL SAFETY LAYER (Always highest priority)
        if (sentiment.isCrisis()) {
            return extremeSupportStrategy;
        }

        // 2. WEIGHTED STRATEGY SELECTION
        // Calculate weights for all applicable strategies
        List<StrategyMatch> matches = new ArrayList<>();

        // Add weights based on sentiment and confidence
        if (sentiment.isAngry()) {
            matches.add(new StrategyMatch("Angry", sentiment.getConfidence(), "Anger detected"));
        }
        
        if (sentiment.isScared()) {
            matches.add(new StrategyMatch("Scared", sentiment.getConfidence(), "Fear/anxiety detected"));
        }
        
        if (sentiment.isSad()) {
            matches.add(new StrategyMatch("Sad", sentiment.getConfidence(), "Sadness detected"));
        }
        
        if (sentiment.isExhausted()) {
            matches.add(new StrategyMatch("Exhausted", sentiment.getConfidence(), "Exhaustion detected"));
        }
        
        if (sentiment.isRomantic()) {
            float weight = sentiment.getConfidence() > 0.7f ? sentiment.getConfidence() : sentiment.getConfidence() * 0.7f;
            matches.add(new StrategyMatch("Romantic", weight, "Romance detected"));
        }
        
        if (sentiment.isExcited()) {
            matches.add(new StrategyMatch("Excited", sentiment.getConfidence(), "Excitement detected"));
        }
        
        if (sentiment.isNostalgic()) {
            matches.add(new StrategyMatch("Nostalgic", sentiment.getConfidence(), "Nostalgia detected"));
        }
        
        if (sentiment.isThoughtful()) {
            matches.add(new StrategyMatch("Thoughtful", sentiment.getConfidence(), "Thoughtfulness detected"));
        }
        
        if (sentiment.isSerious()) {
            matches.add(new StrategyMatch("Serious", sentiment.getConfidence(), "Seriousness detected"));
        }
        
        // General positive sentiment
        if (sentiment.isPositive() && sentiment.getConfidence() >= positiveThreshold) {
            // Split weight between Friendly and Humorous
            float weight = sentiment.getConfidence() * 0.5f;
            matches.add(new StrategyMatch("Friendly", weight, "Positive sentiment"));
            matches.add(new StrategyMatch("Humorous", weight * 0.8f, "Positive sentiment (humorous variant)"));
        }
        
        // General negative sentiment (but not specific emotions)
        if (sentiment.isNegative() && sentiment.getConfidence() >= negativeThreshold) {
            // Use Neutral for de-escalation unless specific emotion is stronger
            matches.add(new StrategyMatch("Neutral", sentiment.getConfidence() * 0.6f, "Negative sentiment (de-escalation)"));
        }
        
        // If we have matches, sort by weight and select the highest
        if (!matches.isEmpty()) {
            Collections.sort(matches); // Sorts in descending order
            StrategyMatch best = matches.get(0);
            
            // Log the selection for debugging
            System.out.printf("[Strategy] Selected: %s (weight: %.2f) - %s%n", 
                best.getStrategyName(), best.getWeight(), best.getReason());
            
            return getStrategyByName(best.getStrategyName());
        }

        // 3. DEFAULT
        return neutralStrategy;
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
            case "angry", "mad" -> angryStrategy;
            case "excited", "thrilled" -> excitedStrategy;
            case "scared", "afraid", "anxious" -> scaredStrategy;
            case "thoughtful", "thinking" -> thoughtfulStrategy;
            case "serious", "professional" -> seriousStrategy;
            case "exhausted", "tired" -> exhaustedStrategy;
            default -> neutralStrategy;
        };
    }
}