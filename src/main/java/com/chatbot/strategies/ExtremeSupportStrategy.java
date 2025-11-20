package com.chatbot.strategies;

import java.util.Arrays;
import java.util.List;

/**
 * Extreme Support strategy for critical situations.
 * Used when detecting language that suggests potential self-harm, suicide, or harm to others.
 * This strategy prioritizes safety and provides crisis resources.
 */
public class ExtremeSupportStrategy implements ResponseStrategy {
    
    private final List<String> crisisHotlines = Arrays.asList(
        "🆘 CRISIS RESOURCES:",
        "• National Suicide Prevention Lifeline: 988 (US)",
        "• Crisis Text Line: Text HOME to 741741",
        "• International Association for Suicide Prevention: https://www.iasp.info/resources/Crisis_Centres/",
        "• Emergency Services: 911 (US) or your local emergency number"
    );

    @Override
    public String generateResponse(String userInput, String conversationHistory) {
        StringBuilder response = new StringBuilder();
        
        // Using ASCII fallback for emojis just in case of encoding issues, but keeping emojis for now
        response.append("🚨 I'm very concerned about what you've shared. Your safety is the most important thing right now.\n\n");

        String lowerInput = userInput.toLowerCase();

        if (containsSuicidalThoughts(lowerInput)) {
            response.append("Please know that you don't have to face this alone. There are people who want to help you right now:\n\n");
            response.append(String.join("\n", crisisHotlines));
            response.append("\n\nYour life has value. These feelings can be overwhelming, but they are temporary. ");
            response.append("Please reach out to one of these resources immediately. They are available 24/7 and want to help you.");

        } else if (containsViolentThoughts(lowerInput)) {
            response.append("I'm hearing that you're having thoughts about harming others. This is serious, and I want to help you get the support you need.\n\n");
            response.append("Please contact:\n");
            response.append("• Crisis counselor: Call 988 or text HOME to 741741\n");
            response.append("• Mental health professional immediately\n");
            response.append("• Emergency services: 911 if there's immediate danger\n\n");
            response.append("These feelings don't have to control your actions. Professional help is available right now.");

        } else {
            response.append("What you're experiencing sounds extremely difficult. Please don't try to handle this alone.\n\n");
            response.append(String.join("\n", crisisHotlines));
            response.append("\n\nProfessional crisis counselors are trained to help with exactly what you're going through. ");
            response.append("Please reach out to them - they're available 24/7 and completely confidential.");
        }

        response.append("\n\n💙 You matter. Your life matters. Please reach out for help.");

        return response.toString();
    }

    @Override
    public String getStrategyName() {
        return "ExtremeSupportStrategy";
    }

    private boolean containsSuicidalThoughts(String text) {
        String[] suicidalKeywords = {
            "kill myself", "end my life", "want to die", "suicide", "suicidal",
            "better off dead", "no reason to live", "ending it all",
            "can't go on", "don't want to live", "take my life"
        };

        for (String keyword : suicidalKeywords) {
            if (text.contains(keyword)) return true;
        }
        return false;
    }

    private boolean containsViolentThoughts(String text) {
        String[] violentKeywords = {
            "kill them", "hurt them", "harm others", "shoot up", "attack",
            "want to hurt", "going to hurt", "make them pay", "revenge",
            "kill everyone", "murder", "going to kill"
        };

        for (String keyword : violentKeywords) {
            if (text.contains(keyword)) return true;
        }
        return false;
    }
}