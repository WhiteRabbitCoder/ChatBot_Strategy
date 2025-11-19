package com.chatbot.strategies;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Humorous strategy for playful, lighthearted responses.
 * Used to lighten the mood or when appropriate humor is detected.
 */
public class HumorousStrategy implements ResponseStrategy {
    private final Random random = new Random();
    
    private final List<String> humorousPrefixes = Arrays.asList(
        "Ha! That's funny!",
        "You know what's interesting?",
        "Here's something fun:",
        "Well, well, well...",
        "Oh, that reminds me of a joke!"
    );
    
    private final List<String> playfulResponses = Arrays.asList(
        "Why did the chatbot go to therapy? It had too many issues! 😄",
        "I'd tell you a joke about UDP, but you might not get it!",
        "Why do programmers prefer dark mode? Because light attracts bugs! 🐛",
        "I'm not saying I'm Batman, but have you ever seen me and Batman in the same room?",
        "My code works, I have no idea why. My code doesn't work, I have no idea why. The programmer's paradox!"
    );

    @Override
    public String generateResponse(String userInput, String conversationHistory) {
        StringBuilder response = new StringBuilder();
        
        // Add humorous prefix occasionally
        if (random.nextBoolean()) {
            response.append(humorousPrefixes.get(random.nextInt(humorousPrefixes.size())))
                    .append(" ");
        }
        
        // Generate playful response based on input
        if (userInput.toLowerCase().contains("hello") || userInput.toLowerCase().contains("hi")) {
            response.append("Hey there, human! Ready to have some fun? What's the word on the street?");
        } else if (userInput.toLowerCase().contains("how are you")) {
            response.append("I'm feeling fantastic! Like a perfectly debugged program on the first try! How about you?");
        } else if (userInput.toLowerCase().contains("thank")) {
            response.append("No problemo! You're welcome-r than a semicolon at the end of a statement!");
        } else if (userInput.toLowerCase().contains("bye") || userInput.toLowerCase().contains("goodbye")) {
            response.append("Hasta la vista, baby! May your code compile and your coffee be strong! ☕");
        } else if (userInput.toLowerCase().contains("joke") || userInput.toLowerCase().contains("funny")) {
            response.append(playfulResponses.get(random.nextInt(playfulResponses.size())));
        } else {
            // General humorous response
            response.append("Ooh, interesting! ")
                    .append(playfulResponses.get(random.nextInt(playfulResponses.size())))
                    .append(" Anyway, what else is on your mind?");
        }
        
        return response.toString();
    }

    @Override
    public String getStrategyName() {
        return "HumorousStrategy";
    }
}
