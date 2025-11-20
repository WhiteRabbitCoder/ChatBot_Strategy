package com.chatbot;

import com.chatbot.model.Sentiment;
import com.chatbot.services.MoodDetectionService;
import com.chatbot.strategies.ResponseStrategy;
import com.chatbot.strategies.StrategySelector;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Main ChatBot orchestrator that coordinates mood detection, strategy selection,
 * and conversation management.
 */
public class ChatBot {
    private final MoodDetectionService moodDetectionService;
    private final StrategySelector strategySelector;
    private final List<String> conversationHistory;
    private final int maxHistorySize = 5;
    
    public ChatBot(String modelPath) {
        this.moodDetectionService = new MoodDetectionService(modelPath);
        this.strategySelector = new StrategySelector();
        this.conversationHistory = new ArrayList<>();
        
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║   ChatBot with Strategy Pattern + ONNX NLP                ║");
        System.out.println("║   CPU-Optimized Sentiment Analysis                        ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println();
    }
    
    /**
     * Process a user message and generate a response.
     */
    public String processMessage(String userInput) {
        if (userInput == null || userInput.trim().isEmpty()) {
            return "I didn't catch that. Could you say something?";
        }
        
        // Step 1: Detect mood using ONNX
        Sentiment sentiment = moodDetectionService.detectMood(userInput);
        System.out.println("[Debug] Detected sentiment: " + sentiment);
        
        // Step 2: Select appropriate strategy based on sentiment
        ResponseStrategy strategy = strategySelector.selectStrategy(sentiment);
        System.out.println("[Debug] Selected strategy: " + strategy.getStrategyName());
        
        // Step 3: Get conversation context
        String context = getConversationContext();
        
        // Step 4: Generate response using selected strategy
        String response = strategy.generateResponse(userInput, context);
        
        // Step 5: Update conversation history
        updateHistory("User: " + userInput);
        updateHistory("Bot: " + response);
        
        return response;
    }
    
    /**
     * Get recent conversation context as a string.
     */
    private String getConversationContext() {
        if (conversationHistory.isEmpty()) {
            return "";
        }
        return String.join("\n", conversationHistory);
    }
    
    /**
     * Update conversation history, maintaining a maximum size.
     */
    private void updateHistory(String message) {
        conversationHistory.add(message);
        if (conversationHistory.size() > maxHistorySize * 2) {
            conversationHistory.remove(0);
        }
    }
    
    /**
     * Start an interactive chat session.
     */
    public void startChat() {y
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("ChatBot is ready! Type your messages below.");
        System.out.println("Type 'exit', 'quit', or 'bye' to end the conversation.");
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println();
        
        while (true) {
            System.out.print("You: ");
            String userInput = scanner.nextLine().trim();
            
            if (userInput.isEmpty()) {
                continue;
            }
            
            // Check for exit commands
            if (userInput.equalsIgnoreCase("exit") || 
                userInput.equalsIgnoreCase("quit") || 
                userInput.equalsIgnoreCase("bye")) {
                
                String farewell = processMessage(userInput);
                System.out.println("Bot: " + farewell);
                System.out.println();
                System.out.println("Thank you for chatting! Goodbye! 👋");
                break;
            }
            
            // Process message and get response
            String response = processMessage(userInput);
            System.out.println("Bot: " + response);
            System.out.println();
        }
        
        scanner.close();
        shutdown();
    }
    
    /**
     * Shutdown and cleanup resources.
     */
    public void shutdown() {
        moodDetectionService.close();
        System.out.println("ChatBot shutdown complete.");
    }
    
    /**
     * Main entry point for the application.
     */
    public static void main(String[] args) {
        // Check for model path argument
        String modelPath = null;
        if (args.length > 0) {
            modelPath = args[0];
            System.out.println("Using ONNX model: " + modelPath);
        } else {
            System.out.println("No ONNX model provided. Running with rule-based sentiment analysis.");
            System.out.println("Usage: java -jar chatbot-strategy.jar [path-to-onnx-model]");
        }
        System.out.println();
        
        // Create and start chatbot
        ChatBot chatBot = new ChatBot(modelPath);
        chatBot.startChat();
    }
}
