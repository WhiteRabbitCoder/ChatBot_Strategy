package com.chatbot.strategies;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Thoughtful strategy for contemplative, reflective responses.
 * Used when thoughtfulness or deep thinking is detected.
 */
public class ThoughtfulStrategy implements ResponseStrategy {
    private final Random random = new Random();
    
    private final List<String> thoughtfulPrefixes = Arrays.asList(
        "That's a thoughtful question.",
        "You're really thinking deeply about this.",
        "I appreciate your contemplative approach.",
        "That requires careful consideration.",
        "You're being very reflective."
    );
    
    private final List<String> reflectiveMessages = Arrays.asList(
        "It's wonderful to see you taking time to think things through carefully.",
        "Deep thinking often leads to the most meaningful insights.",
        "Your contemplative nature shows wisdom. What conclusions are you drawing?",
        "Sometimes the best answers come from quiet reflection. What are you pondering?",
        "Thoughtfulness is a valuable quality. Let's explore this idea together."
    );

    @Override
    public String generateResponse(String userInput, String conversationHistory) {
        StringBuilder response = new StringBuilder();
        
        // Add thoughtful prefix
        response.append(thoughtfulPrefixes.get(random.nextInt(thoughtfulPrefixes.size())))
                .append(" ");
        
        // Generate reflective response based on input
        String lowerInput = userInput.toLowerCase();
        
        if (lowerInput.contains("thinking") || lowerInput.contains("pensando")) {
            response.append("Deep thought leads to clarity. What aspects are you weighing? Share your thoughts with me.");
        } else if (lowerInput.contains("wonder") || lowerInput.contains("wondering") || lowerInput.contains("pregunto")) {
            response.append("Curiosity and wonder are the seeds of understanding. What possibilities are you exploring?");
        } else if (lowerInput.contains("considering") || lowerInput.contains("pensativo")) {
            response.append("Taking time to consider all angles is wise. What factors are most important to you?");
        } else if (lowerInput.contains("reflect") || lowerInput.contains("reflection") || lowerInput.contains("reflexión")) {
            response.append("Reflection allows us to learn from experience. What insights are emerging for you?");
        } else {
            // General reflective response
            response.append(reflectiveMessages.get(random.nextInt(reflectiveMessages.size())));
        }
        
        return response.toString();
    }

    @Override
    public String getStrategyName() {
        return "ThoughtfulStrategy";
    }
}
