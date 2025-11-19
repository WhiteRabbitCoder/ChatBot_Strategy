package com.chatbot.model;

/**
 * Represents the detected sentiment/mood from the user's input.
 */
public class Sentiment {
    private final String label;
    private final float confidence;
    
    public Sentiment(String label, float confidence) {
        this.label = label;
        this.confidence = confidence;
    }
    
    public String getLabel() {
        return label;
    }
    
    public float getConfidence() {
        return confidence;
    }
    
    public boolean isPositive() {
        return "POSITIVE".equalsIgnoreCase(label);
    }
    
    public boolean isNegative() {
        return "NEGATIVE".equalsIgnoreCase(label);
    }
    
    public boolean isNeutral() {
        return "NEUTRAL".equalsIgnoreCase(label) || 
               (!isPositive() && !isNegative());
    }
    
    @Override
    public String toString() {
        return String.format("Sentiment{label='%s', confidence=%.2f}", label, confidence);
    }
}
