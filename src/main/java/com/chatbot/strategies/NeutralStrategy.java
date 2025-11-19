package com.chatbot.strategies;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Neutral strategy for balanced, informative responses.
 * Used when sentiment is neutral or unclear.
 */
public class NeutralStrategy implements ResponseStrategy {
    private final Random random = new Random();
    
    private final List<String> neutralPrefixes = Arrays.asList(
        "I understand.",
        "I see.",
        "That's interesting.",
        "Noted.",
        "Okay."
    );
    
    private final List<String> followUpQuestions = Arrays.asList(
        "Could you tell me more about that?",
        "What else would you like to discuss?",
        "Is there anything specific you'd like to know?",
        "How can I help you further?",
        "What are your thoughts on this?"
    );

    @Override
    public String generateResponse(String userInput, String conversationHistory) {
        StringBuilder response = new StringBuilder();
        
        // Add contextual prefix
        if (!conversationHistory.isEmpty() && random.nextBoolean()) {
            response.append(neutralPrefixes.get(random.nextInt(neutralPrefixes.size())))
                    .append(" ");
        }
        
        // Generate main response based on input
        if (userInput.toLowerCase().contains("hello") || userInput.toLowerCase().contains("hi")) {
            response.append("Hello. How can I assist you today?");
        } else if (userInput.toLowerCase().contains("how are you")) {
            response.append("I'm functioning as expected. How about you?");
        } else if (userInput.toLowerCase().contains("thank")) {
            response.append("You're welcome. Is there anything else I can help with?");
        } else if (userInput.toLowerCase().contains("bye") || userInput.toLowerCase().contains("goodbye")) {
            response.append("Goodbye. Feel free to return if you need assistance.");
        } else {
            // General neutral response
            response.append("I acknowledge your message. ")
                    .append(followUpQuestions.get(random.nextInt(followUpQuestions.size())));
        }
        
        return response.toString();
    }

    @Override
    public String getStrategyName() {
        return "NeutralStrategy";
    }
}
