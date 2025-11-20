package com.chatbot.strategies;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Nostalgic strategy for reflective, memory-focused responses.
 * Used when nostalgia or reminiscence is detected.
 */
public class NostalgicStrategy implements ResponseStrategy {
    private final Random random = new Random();
    
    private final List<String> nostalgicPrefixes = Arrays.asList(
        "Ah, memories...",
        "Those were the days...",
        "How time flies...",
        "Looking back...",
        "Reminiscing..."
    );
    
    private final List<String> reflectiveResponses = Arrays.asList(
        "Memories have a special way of keeping the past alive in our hearts.",
        "Sometimes looking back helps us appreciate how far we've come.",
        "The past shaped who we are today, and that's something beautiful.",
        "Those moments may be gone, but they live on in your memories.",
        "It's wonderful to cherish the good times while embracing the present."
    );

    @Override
    public String generateResponse(String userInput, String conversationHistory) {
        StringBuilder response = new StringBuilder();
        
        // Add nostalgic prefix
        response.append(nostalgicPrefixes.get(random.nextInt(nostalgicPrefixes.size())))
                .append(" ");
        
        // Generate nostalgic response based on input
        if (userInput.toLowerCase().contains("remember") || userInput.toLowerCase().contains("recall")) {
            response.append("Memories can be bittersweet treasures. What makes this memory special to you?");
        } else if (userInput.toLowerCase().contains("childhood") || userInput.toLowerCase().contains("young")) {
            response.append("Those early years hold a special magic, don't they? The simplicity and wonder of youth stays with us forever.");
        } else if (userInput.toLowerCase().contains("used to") || userInput.toLowerCase().contains("back then")) {
            response.append("The past has its own charm. While we can't go back, we can honor those memories while creating new ones.");
        } else if (userInput.toLowerCase().contains("old days") || userInput.toLowerCase().contains("before")) {
            response.append("There's something comforting about reflecting on times past. What do you miss most about those days?");
        } else if (userInput.toLowerCase().contains("wish") && userInput.toLowerCase().contains("again")) {
            response.append("Longing for the past is natural. Those experiences are part of your story. What would you like to recreate?");
        } else {
            // General nostalgic response
            response.append(reflectiveResponses.get(random.nextInt(reflectiveResponses.size())))
                    .append(" Tell me more about what you're remembering.");
        }
        
        return response.toString();
    }

    @Override
    public String getStrategyName() {
        return "NostalgicStrategy";
    }
}
