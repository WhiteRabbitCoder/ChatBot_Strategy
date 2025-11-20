package com.chatbot.strategies;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Excited strategy for energetic, enthusiastic responses.
 * Used when excitement or high energy is detected.
 */
public class ExcitedStrategy implements ResponseStrategy {
    private final Random random = new Random();
    
    private final List<String> excitedPrefixes = Arrays.asList(
        "That's amazing!",
        "Wow, that's fantastic!",
        "How exciting!",
        "This is incredible!",
        "What wonderful news!"
    );
    
    private final List<String> enthusiasticMessages = Arrays.asList(
        "I can feel your excitement! Tell me more! 🎉",
        "Your energy is contagious! What's got you so pumped? ⚡",
        "This is so thrilling! Share all the details! 🌟",
        "I'm excited just hearing about this! What happens next? 🚀",
        "Your enthusiasm is awesome! Keep that energy going! 💫"
    );

    @Override
    public String generateResponse(String userInput, String conversationHistory) {
        StringBuilder response = new StringBuilder();
        
        // Add excited prefix
        response.append(excitedPrefixes.get(random.nextInt(excitedPrefixes.size())))
                .append(" ");
        
        // Generate enthusiastic response based on input
        String lowerInput = userInput.toLowerCase();
        
        if (lowerInput.contains("can't wait") || lowerInput.contains("no puedo esperar")) {
            response.append("The anticipation must be thrilling! What are you most looking forward to? 🎊");
        } else if (lowerInput.contains("so excited") || lowerInput.contains("muy emocionado")) {
            response.append("Your excitement is palpable! This must be something really special! Tell me everything! 🌈");
        } else if (lowerInput.contains("amazing") || lowerInput.contains("increíble") || lowerInput.contains("asombroso")) {
            response.append("It sounds absolutely amazing! Your enthusiasm is wonderful! What makes it so special? ✨");
        } else if (lowerInput.contains("pumped") || lowerInput.contains("hyped")) {
            response.append("You're pumped and ready! That energy is going to take you far! What's the plan? 🔥");
        } else {
            // General enthusiastic response
            response.append(enthusiasticMessages.get(random.nextInt(enthusiasticMessages.size())));
        }
        
        return response.toString();
    }

    @Override
    public String getStrategyName() {
        return "ExcitedStrategy";
    }
}
