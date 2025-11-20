package com.chatbot.strategies;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Neutral strategy for balanced, informative responses.
 * UPDATED: Includes prioritized greeting detection, conflict de-escalation, and varied conversational fillers.
 */
public class NeutralStrategy implements ResponseStrategy {
    private final Random random = new Random();

    // 1. Frases cortas para iniciar (Fillers)
    private final List<String> neutralPrefixes = Arrays.asList(
        "I see.", "Understood.", "Right.", "That makes sense.",
        "Got it.", "Interesting point.", "I hear you.", "Fair enough.", "Noted."
    );

    // 2. Respuestas genéricas para mantener el flujo
    private final List<String> generalResponses = Arrays.asList(
        "I'm listening, please go on.",
        "I'm here to help if you need anything specific.",
        "That's a valid perspective.",
        "I'm processing what you've shared.",
        "Could you expand a bit more on that?",
        "I appreciate you sharing that with me.",
        "I'm following your train of thought.",
        "Let's explore that a bit further."
    );

    // 3. Preguntas de seguimiento (Opcionales)
    private final List<String> followUpQuestions = Arrays.asList(
        "Could you tell me more details?",
        "What else is on your mind?",
        "Is there a specific outcome you're looking for?",
        "How can I best assist you with this?",
        "What are your thoughts on the next steps?",
        "Would you like to change the topic?"
    );

    // 4. Respuestas para conflicto/enfado (Apology/De-escalation)
    private final List<String> apologyResponses = Arrays.asList(
        "I apologize if I caused frustration. How can I make this right?",
        "I'm sorry to hear that. Please tell me how I can improve.",
        "I didn't mean to upset you. Let's try to start over.",
        "I understand your frustration. How can I help resolve this?",
        "I hear that you're upset. I want to be helpful, so please let me know what you need."
    );

    @Override
    public String generateResponse(String userInput, String conversationHistory) {
        String lowerInput = userInput.toLowerCase().trim();

        // --- PRIORIDAD 0: SALUDOS CLAROS (BLINDADO) ---
        // Detectar saludos explícitos antes de cualquier otra lógica
        if (lowerInput.equals("hi") || lowerInput.equals("hello") ||
            lowerInput.equals("hi!") || lowerInput.equals("hello!") ||
            lowerInput.startsWith("hi ") || lowerInput.startsWith("hello ") ||
            lowerInput.equals("hey") || lowerInput.startsWith("hey ")) {

            return "Hello! How can I help you today?";
        }

        // --- PRIORIDAD 1: MANEJO DE CONFLICTOS ---
        if (lowerInput.contains("mad") || lowerInput.contains("angry") ||
            lowerInput.contains("hate") || lowerInput.contains("stupid") ||
            lowerInput.contains("don't like") || lowerInput.contains("annoying") ||
            lowerInput.contains("useless")) {

            return apologyResponses.get(random.nextInt(apologyResponses.size()));
        }

        // --- PRIORIDAD 2: PREGUNTAS COMUNES ESPECÍFICAS ---
        if (lowerInput.contains("how are you")) {
            return "I'm functioning perfectly, thank you. How can I help you?";
        }
        if (lowerInput.contains("thank")) {
            return "You're very welcome.";
        }
        if (lowerInput.contains("bye") || lowerInput.contains("goodbye")) {
            return "Goodbye! Feel free to come back anytime.";
        }

        // --- PRIORIDAD 3: GENERACIÓN DINÁMICA (Mix & Match) ---
        StringBuilder response = new StringBuilder();

        // A. Prefijo (50% de probabilidad)
        if (random.nextBoolean()) {
            String prefix = neutralPrefixes.get(random.nextInt(neutralPrefixes.size()));
            response.append(prefix).append(" ");
        }

        // B. Respuesta Base (Siempre)
        String baseResponse = generalResponses.get(random.nextInt(generalResponses.size()));
        response.append(baseResponse);

        // C. Pregunta de Seguimiento (30% de probabilidad para no ser pesado)
        if (random.nextInt(10) < 3) {
            response.append(" ");
            response.append(followUpQuestions.get(random.nextInt(followUpQuestions.size())));
        }

        return response.toString();
    }

    @Override
    public String getStrategyName() {
        return "NeutralStrategy";
    }
}