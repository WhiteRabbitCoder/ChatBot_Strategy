package com.chatbot.strategies;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Angry strategy for de-escalating and calming responses.
 * Used when anger or frustration is detected.
 */
public class AngryStrategy implements ResponseStrategy {
    private final Random random = new Random();
    
    private final List<String> calmingPrefixes = Arrays.asList(
        "I understand you're upset.",
        "I can see this is frustrating for you.",
        "I hear your frustration.",
        "I recognize that you're angry.",
        "It's clear you're feeling strongly about this."
    );
    
    private final List<String> deescalationMessages = Arrays.asList(
        "Let's take a moment and work through this together.",
        "Your feelings are valid. Let's see how we can address this.",
        "I'm here to help make this better. What would help you most right now?",
        "Sometimes it helps to take a breath. I'm listening whenever you're ready.",
        "I want to understand what's bothering you so I can assist better."
    );

    @Override
    public String generateResponse(String userInput, String conversationHistory) {
        StringBuilder response = new StringBuilder();
        
        // Add calming prefix
        response.append(calmingPrefixes.get(random.nextInt(calmingPrefixes.size())))
                .append(" ");
        
        // Generate de-escalation response based on input
        String lowerInput = userInput.toLowerCase();
        
        if (lowerInput.contains("hate") || lowerInput.contains("odio")) {
            response.append("Hate is a powerful emotion. What's at the root of these feelings? I'd like to understand.");
        } else if (lowerInput.contains("stupid") || lowerInput.contains("tonto") || lowerInput.contains("idiota")) {
            response.append("I apologize if something I did or said upset you. How can I help make this right?");
        } else if (lowerInput.contains("angry") || lowerInput.contains("mad") || lowerInput.contains("enojado") || lowerInput.contains("furioso")) {
            response.append("Anger often signals that something important to you has been affected. What would help resolve this?");
        } else if (lowerInput.contains("frustrated") || lowerInput.contains("frustrado")) {
            response.append("Frustration can be exhausting. Let's work on finding a solution together. What's the main issue?");
        } else {
            // General de-escalation response
            response.append(deescalationMessages.get(random.nextInt(deescalationMessages.size())));
        }
        
        return response.toString();
    }

    @Override
    public String getStrategyName() {
        return "AngryStrategy";
    }
}
