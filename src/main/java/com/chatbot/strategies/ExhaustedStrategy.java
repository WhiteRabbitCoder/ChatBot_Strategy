package com.chatbot.strategies;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Exhausted strategy for supportive, understanding responses.
 * Used when tiredness or exhaustion is detected.
 */
public class ExhaustedStrategy implements ResponseStrategy {
    private final Random random = new Random();
    
    private final List<String> empatheticPrefixes = Arrays.asList(
        "You sound exhausted.",
        "I can hear the tiredness in your words.",
        "Burnout is real and valid.",
        "It sounds like you're running on empty.",
        "You seem really drained."
    );
    
    private final List<String> supportiveMessages = Arrays.asList(
        "Rest isn't weakness—it's essential. What can you do to give yourself a break?",
        "Being exhausted is a sign you've been pushing hard. What would help you recharge?",
        "Your well-being matters. Is there any way you can lighten your load?",
        "Fatigue affects everything. Have you been able to rest at all?",
        "Sometimes we need permission to slow down. It's okay to take care of yourself."
    );

    @Override
    public String generateResponse(String userInput, String conversationHistory) {
        StringBuilder response = new StringBuilder();
        
        // Add empathetic prefix
        response.append(empatheticPrefixes.get(random.nextInt(empatheticPrefixes.size())))
                .append(" ");
        
        // Generate supportive response based on input
        String lowerInput = userInput.toLowerCase();
        
        if (lowerInput.contains("tired") || lowerInput.contains("cansado") || lowerInput.contains("exhausted")) {
            response.append("Exhaustion takes a toll on body and mind. When was the last time you had real rest? You deserve it.");
        } else if (lowerInput.contains("burnout") || lowerInput.contains("burnt out") || lowerInput.contains("agotado")) {
            response.append("Burnout is serious. Your body is telling you it needs recovery time. What's one small thing you could do for yourself today?");
        } else if (lowerInput.contains("can't anymore") || lowerInput.contains("no puedo más")) {
            response.append("Reaching your limit is human. It's okay to acknowledge you can't do everything. What support do you need?");
        } else if (lowerInput.contains("sleep") || lowerInput.contains("dormir")) {
            response.append("Sleep deprivation affects everything—mood, focus, health. Prioritizing rest isn't selfish, it's necessary. Can you get some rest?");
        } else {
            // General supportive response
            response.append(supportiveMessages.get(random.nextInt(supportiveMessages.size())));
        }
        
        return response.toString();
    }

    @Override
    public String getStrategyName() {
        return "ExhaustedStrategy";
    }
}
