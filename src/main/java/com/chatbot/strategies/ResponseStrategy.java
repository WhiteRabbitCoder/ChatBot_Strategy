package com.chatbot.strategies;

/**
 * Strategy interface for generating chatbot responses.
 * Implementations define different response behaviors based on detected mood.
 */
public interface ResponseStrategy {
    /**
     * Generate a response based on user input and conversation context.
     * 
     * @param userInput The user's message
     * @param conversationHistory Recent conversation history for context
     * @return The chatbot's response
     */
    String generateResponse(String userInput, String conversationHistory);
    
    /**
     * Get the name of this strategy for logging/debugging.
     * 
     * @return Strategy name
     */
    String getStrategyName();
}
