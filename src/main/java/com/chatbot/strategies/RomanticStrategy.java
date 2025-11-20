package com.chatbot.strategies;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Romantic strategy for affectionate, loving responses.
 * Used when romantic or loving sentiment is detected.
 */
public class RomanticStrategy implements ResponseStrategy {
    private final Random random = new Random();
    
    private final List<String> romanticPrefixes = Arrays.asList(
        "How lovely!",
        "That's beautiful!",
        "How sweet!",
        "That touches my heart!",
        "How romantic!"
    );
    
    private final List<String> romanticResponses = Arrays.asList(
        "Love is such a wonderful feeling. It makes everything brighter! 💕",
        "What a beautiful sentiment. Love truly makes the world go round! ❤️",
        "How sweet! There's nothing quite like the feeling of being in love.",
        "That's so heartwarming! Love is the most precious thing we have.",
        "What a tender thought. Love is what makes life worth living! 💗"
    );

    @Override
    public String generateResponse(String userInput, String conversationHistory) {
        StringBuilder response = new StringBuilder();
        
        // Add romantic prefix occasionally
        if (random.nextBoolean()) {
            response.append(romanticPrefixes.get(random.nextInt(romanticPrefixes.size())))
                    .append(" ");
        }
        
        // Generate romantic response based on input
        if (userInput.toLowerCase().contains("love") || userInput.toLowerCase().contains("amor")) {
            response.append(romanticResponses.get(random.nextInt(romanticResponses.size())));
        } else if (userInput.toLowerCase().contains("heart") || userInput.toLowerCase().contains("corazón")) {
            response.append("Matters of the heart are the most beautiful. Tell me more about what's in yours! 💖");
        } else if (userInput.toLowerCase().contains("kiss") || userInput.toLowerCase().contains("beso")) {
            response.append("A kiss is a lovely expression of affection! How romantic! 😘");
        } else if (userInput.toLowerCase().contains("beautiful") || userInput.toLowerCase().contains("hermoso")) {
            response.append("Beauty is all around us, especially when we're with those we love! ✨");
        } else {
            // General romantic response
            response.append("There's something special in the air today. ")
                    .append("Would you like to share what's making you feel this way? 💝");
        }
        
        return response.toString();
    }

    @Override
    public String getStrategyName() {
        return "RomanticStrategy";
    }
}
