package com.chatbot.strategies;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Sad strategy for empathetic, supportive responses.
 * Used when sadness or melancholy is detected.
 */
public class SadStrategy implements ResponseStrategy {
    private final Random random = new Random();
    
    private final List<String> empatheticPrefixes = Arrays.asList(
        "I'm sorry to hear that.",
        "That must be difficult.",
        "I understand this is hard.",
        "I can sense your sadness.",
        "That sounds tough."
    );
    
    private final List<String> supportiveMessages = Arrays.asList(
        "It's okay to feel sad sometimes. These feelings are valid.",
        "Remember, even dark clouds eventually pass. Brighter days are ahead.",
        "Take all the time you need. It's important to process your feelings.",
        "You're not alone in feeling this way. I'm here to listen.",
        "Allow yourself to feel these emotions. Healing takes time."
    );

    @Override
    public String generateResponse(String userInput, String conversationHistory) {
        StringBuilder response = new StringBuilder();
        
        // Add empathetic prefix
        response.append(empatheticPrefixes.get(random.nextInt(empatheticPrefixes.size())))
                .append(" ");
        
        // Generate supportive response based on input
        if (userInput.toLowerCase().contains("cry") || userInput.toLowerCase().contains("tears")) {
            response.append("It's completely okay to cry. Tears can be healing. Would you like to talk about what's bothering you?");
        } else if (userInput.toLowerCase().contains("alone") || userInput.toLowerCase().contains("lonely")) {
            response.append("Loneliness can be so heavy. Please remember you're not truly alone. I'm here, and there are people who care about you.");
        } else if (userInput.toLowerCase().contains("miss") || userInput.toLowerCase().contains("gone")) {
            response.append("Missing someone shows how much they meant to you. Those memories and feelings are precious, even when they hurt.");
        } else if (userInput.toLowerCase().contains("hurt") || userInput.toLowerCase().contains("pain")) {
            response.append("Emotional pain is real and valid. Be gentle with yourself as you work through this.");
        } else {
            // General supportive response
            response.append(supportiveMessages.get(random.nextInt(supportiveMessages.size())))
                    .append(" Would you like to share more about how you're feeling?");
        }
        
        return response.toString();
    }

    @Override
    public String getStrategyName() {
        return "SadStrategy";
    }
}
