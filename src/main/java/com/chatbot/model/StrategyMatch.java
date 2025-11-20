package com.chatbot.model;

/**
 * Represents a strategy match with a confidence weight.
 * Used for weighted strategy selection.
 */
public class StrategyMatch implements Comparable<StrategyMatch> {
    private final String strategyName;
    private final float weight;
    private final String reason;
    
    public StrategyMatch(String strategyName, float weight, String reason) {
        this.strategyName = strategyName;
        this.weight = weight;
        this.reason = reason;
    }
    
    public String getStrategyName() {
        return strategyName;
    }
    
    public float getWeight() {
        return weight;
    }
    
    public String getReason() {
        return reason;
    }
    
    @Override
    public int compareTo(StrategyMatch other) {
        // Sort in descending order (highest weight first)
        return Float.compare(other.weight, this.weight);
    }
    
    @Override
    public String toString() {
        return String.format("StrategyMatch{strategy='%s', weight=%.2f, reason='%s'}", 
            strategyName, weight, reason);
    }
}
