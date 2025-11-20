package com.chatbot.strategies;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Serious strategy for professional, focused responses.
 * Used when seriousness or professional tone is detected.
 */
public class SeriousStrategy implements ResponseStrategy {
    private final Random random = new Random();
    
    private final List<String> professionalPrefixes = Arrays.asList(
        "I understand the gravity of this.",
        "This is clearly important.",
        "I take this seriously.",
        "Let's address this properly.",
        "I recognize the importance of this matter."
    );
    
    private final List<String> focusedMessages = Arrays.asList(
        "I'm fully focused on helping you with this. Please provide all relevant details.",
        "This deserves our complete attention. What specific aspects should we address first?",
        "I'm committed to helping you resolve this matter effectively.",
        "Let's approach this systematically. What are the key priorities?",
        "I'm here to provide you with serious, thoughtful assistance."
    );

    @Override
    public String generateResponse(String userInput, String conversationHistory) {
        StringBuilder response = new StringBuilder();
        
        // Add professional prefix
        response.append(professionalPrefixes.get(random.nextInt(professionalPrefixes.size())))
                .append(" ");
        
        // Generate focused response based on input
        String lowerInput = userInput.toLowerCase();
        
        if (lowerInput.contains("important") || lowerInput.contains("crucial") || lowerInput.contains("importante")) {
            response.append("Given the importance of this matter, let's ensure we address it thoroughly. What are the critical elements?");
        } else if (lowerInput.contains("serious") || lowerInput.contains("grave") || lowerInput.contains("serio")) {
            response.append("I understand this is a serious matter. I'm here to provide focused, professional assistance. How can I help?");
        } else if (lowerInput.contains("professional") || lowerInput.contains("business") || lowerInput.contains("profesional")) {
            response.append("I'll maintain a professional approach to ensure we handle this appropriately. What do you need?");
        } else if (lowerInput.contains("urgent") || lowerInput.contains("urgente")) {
            response.append("Urgency noted. Let's prioritize the most critical aspects. What needs immediate attention?");
        } else {
            // General focused response
            response.append(focusedMessages.get(random.nextInt(focusedMessages.size())));
        }
        
        return response.toString();
    }

    @Override
    public String getStrategyName() {
        return "SeriousStrategy";
    }
}
