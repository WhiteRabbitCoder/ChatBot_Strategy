package com.chatbot.services;

import java.util.Arrays;

public class MoodDetectionService {
    
    // Keywords for detecting sad mood
    private static final String[] sadKeywords = {
        "so sad", "depressed", "miserable", "hopeless", "heartbroken",
        "sad", "unhappy", "down", "blue", "gloomy", "sorrow", "melancholy"
    };
    
    // Keywords for detecting negative mood
    private static final String[] negativeKeywords = {
        "angry", "frustrated", "disappointed", "hate", "terrible", "awful", "worst",
        "mad", "upset", "annoyed", "bored", "tired", "sick", "pain", "hurt", "stupid", "idiot"
    };
    
    // Keywords for detecting positive mood
    private static final String[] positiveKeywords = {
        "happy", "great", "wonderful", "amazing", "awesome", "fantastic", "good",
        "glad", "cool", "yay", "excited", "fun", "enjoy"
    };
    
    // Keywords for detecting romantic mood
    private static final String[] romanticKeywords = {
        "love", "romance", "romantic", "sweetheart", "darling", "crush"
    };
    
    /**
     * Detects the mood from the input text using a rule-based approach
     * @param text The input text to analyze
     * @return The detected mood as a string
     */
    public String detectMoodRuleBased(String text) {
        if (text == null || text.trim().isEmpty()) {
            return "NEUTRAL";
        }
        
        String lowerText = text.toLowerCase();
        
        // Check for romantic mood
        if (containsAny(lowerText, romanticKeywords)) {
            return "ROMANTIC";
        }
        
        // Check for sad mood
        if (containsAny(lowerText, sadKeywords)) {
            return "SAD";
        }
        
        // Check for negative mood
        if (containsAny(lowerText, negativeKeywords)) {
            return "NEGATIVE";
        }
        
        // Check for positive mood
        if (containsAny(lowerText, positiveKeywords)) {
            return "POSITIVE";
        }
        
        return "NEUTRAL";
    }
    
    /**
     * Checks if the text contains any of the keywords
     * @param text The text to search in
     * @param keywords The keywords to search for
     * @return true if any keyword is found, false otherwise
     */
    private boolean containsAny(String text, String[] keywords) {
        return Arrays.stream(keywords)
                .anyMatch(keyword -> text.contains(keyword));
    }
}
