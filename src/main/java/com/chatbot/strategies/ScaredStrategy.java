package com.chatbot.strategies;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Scared strategy for reassuring, calming responses.
 * Used when fear, anxiety, or nervousness is detected.
 */
public class ScaredStrategy implements ResponseStrategy {
    private final Random random = new Random();
    
    private final List<String> reassuringPrefixes = Arrays.asList(
        "I understand you're feeling scared.",
        "It's okay to feel anxious.",
        "Fear is a natural response.",
        "I hear your worry.",
        "Feeling nervous is completely normal."
    );
    
    private final List<String> calmingMessages = Arrays.asList(
        "Take a deep breath. You're safe right now. Let's work through this together.",
        "Your feelings are valid. What specific thing is worrying you most?",
        "Sometimes fear feels overwhelming, but you're stronger than you know.",
        "Let's break this down into smaller, manageable pieces. What's the first concern?",
        "You don't have to face this alone. I'm here to help you feel more secure."
    );

    @Override
    public String generateResponse(String userInput, String conversationHistory) {
        StringBuilder response = new StringBuilder();
        
        // Add reassuring prefix
        response.append(reassuringPrefixes.get(random.nextInt(reassuringPrefixes.size())))
                .append(" ");
        
        // Generate calming response based on input
        String lowerInput = userInput.toLowerCase();
        
        if (lowerInput.contains("afraid") || lowerInput.contains("scared") || lowerInput.contains("miedo") || lowerInput.contains("asustado")) {
            response.append("Fear can feel paralyzing, but you're safe here. What's making you feel this way? Let's talk through it together.");
        } else if (lowerInput.contains("nervous") || lowerInput.contains("anxious") || lowerInput.contains("nervioso") || lowerInput.contains("ansioso")) {
            response.append("Nervousness is your body's way of preparing you. Take slow, deep breaths. What's causing this anxiety?");
        } else if (lowerInput.contains("worried") || lowerInput.contains("worry") || lowerInput.contains("preocupado")) {
            response.append("Worry shows you care deeply. Let's identify what you can control and what you cannot. What's on your mind?");
        } else if (lowerInput.contains("panic") || lowerInput.contains("pánico")) {
            response.append("If you're feeling panicked, focus on your breathing: inhale for 4, hold for 4, exhale for 4. You're going to be okay. What triggered this?");
        } else {
            // General calming response
            response.append(calmingMessages.get(random.nextInt(calmingMessages.size())));
        }
        
        return response.toString();
    }

    @Override
    public String getStrategyName() {
        return "ScaredStrategy";
    }
}
