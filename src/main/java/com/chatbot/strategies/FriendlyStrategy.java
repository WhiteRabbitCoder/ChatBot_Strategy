package com.chatbot.strategies;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Friendly strategy for warm, supportive responses.
 * Used when sentiment is positive.
 */
public class FriendlyStrategy implements ResponseStrategy {
    private final Random random = new Random();
    
    private final List<String> friendlyPrefixes = Arrays.asList(
        "That's great!",
        "Wonderful!",
        "I'm so glad to hear that!",
        "That's fantastic!",
        "How lovely!"
    );
    
    private final List<String> encouragements = Arrays.asList(
        "Keep up the great work!",
        "You're doing amazing!",
        "That's the spirit!",
        "I'm here to support you!",
        "Keep that positive energy going!"
    );

    @Override
    public String generateResponse(String userInput, String conversationHistory) {
        StringBuilder response = new StringBuilder();
        
        // Add friendly prefix
        response.append(friendlyPrefixes.get(random.nextInt(friendlyPrefixes.size())))
                .append(" ");
        
        // Generate warm response based on input
        if (userInput.toLowerCase().contains("hello") || userInput.toLowerCase().contains("hi")) {
            response.append("Hello there! I'm so happy to chat with you! How's your day going?");
        } else if (userInput.toLowerCase().contains("how are you")) {
            response.append("I'm doing wonderfully, thank you for asking! How are you feeling today?");
        } else if (userInput.toLowerCase().contains("thank")) {
            response.append("You're very welcome! It's my pleasure to help you. Feel free to ask me anything!");
        } else if (userInput.toLowerCase().contains("bye") || userInput.toLowerCase().contains("goodbye")) {
            response.append("It was lovely talking with you! Have a wonderful day and come back anytime!");
        } else if (userInput.toLowerCase().contains("good") || userInput.toLowerCase().contains("great")) {
            response.append("I'm thrilled to hear that! ").append(encouragements.get(random.nextInt(encouragements.size())));
        } else {
            // General friendly response
            response.append("I love your enthusiasm! Tell me more about what's on your mind!");
        }
        
        return response.toString();
    }

    @Override
    public String getStrategyName() {
        return "FriendlyStrategy";
    }
}
