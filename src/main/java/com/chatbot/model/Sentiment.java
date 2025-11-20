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
    
    public boolean isRomantic() {
        return "ROMANTIC".equalsIgnoreCase(label);
    }
    
    public boolean isSad() {
        return "SAD".equalsIgnoreCase(label);
    }
    
    public boolean isNostalgic() {
        return "NOSTALGIC".equalsIgnoreCase(label);
    }
    
    public boolean isCrisis() {
        return "CRISIS".equalsIgnoreCase(label);
    }
    
    public boolean isAngry() {
        return "ANGRY".equalsIgnoreCase(label);
    }
    
    public boolean isExcited() {
        return "EXCITED".equalsIgnoreCase(label);
    }
    
    public boolean isScared() {
        return "SCARED".equalsIgnoreCase(label);
    }
    
    public boolean isThoughtful() {
        return "THOUGHTFUL".equalsIgnoreCase(label);
    }
    
    public boolean isSerious() {
        return "SERIOUS".equalsIgnoreCase(label);
    }
    
    public boolean isExhausted() {
        return "EXHAUSTED".equalsIgnoreCase(label);
    }
    
    @Override
    public String toString() {
        return String.format("Sentiment{label='%s', confidence=%.2f}", label, confidence);
    }
}
