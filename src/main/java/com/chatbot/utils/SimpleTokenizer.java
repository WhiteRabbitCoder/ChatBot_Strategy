package com.chatbot.utils;

import java.util.*;

/**
 * Simple tokenizer for converting text to token IDs.
 * This is a minimal implementation for demonstration purposes.
 * In production, you would use the actual tokenizer vocabulary from your model.
 */
public class SimpleTokenizer {
    private static final int MAX_LENGTH = 128;
    private static final int PAD_TOKEN_ID = 0;
    private static final int CLS_TOKEN_ID = 101;
    private static final int SEP_TOKEN_ID = 102;
    private static final int UNK_TOKEN_ID = 100;
    
    // Basic vocabulary (simplified - in real implementation, load from vocab.txt)
    private final Map<String, Integer> vocab;
    
    public SimpleTokenizer() {
        this.vocab = buildBasicVocabulary();
    }
    
    /**
     * Tokenize text into input IDs, attention mask, and token type IDs.
     * 
     * @param text The input text
     * @return Map containing input_ids, attention_mask, and token_type_ids
     */
    public Map<String, long[]> tokenize(String text) {
        // Convert to lowercase and split into words
        String[] words = text.toLowerCase()
                            .replaceAll("[^a-z0-9\\s]", "")
                            .split("\\s+");
        
        List<Long> inputIds = new ArrayList<>();
        List<Long> attentionMask = new ArrayList<>();
        List<Long> tokenTypeIds = new ArrayList<>();
        
        // Add [CLS] token
        inputIds.add((long) CLS_TOKEN_ID);
        attentionMask.add(1L);
        tokenTypeIds.add(0L);
        
        // Add word tokens
        for (String word : words) {
            if (inputIds.size() >= MAX_LENGTH - 1) break;
            
            int tokenId = vocab.getOrDefault(word, UNK_TOKEN_ID);
            inputIds.add((long) tokenId);
            attentionMask.add(1L);
            tokenTypeIds.add(0L);
        }
        
        // Add [SEP] token
        inputIds.add((long) SEP_TOKEN_ID);
        attentionMask.add(1L);
        tokenTypeIds.add(0L);
        
        // Pad to MAX_LENGTH
        while (inputIds.size() < MAX_LENGTH) {
            inputIds.add((long) PAD_TOKEN_ID);
            attentionMask.add(0L);
            tokenTypeIds.add(0L);
        }
        
        Map<String, long[]> result = new HashMap<>();
        result.put("input_ids", inputIds.stream().mapToLong(Long::longValue).toArray());
        result.put("attention_mask", attentionMask.stream().mapToLong(Long::longValue).toArray());
        result.put("token_type_ids", tokenTypeIds.stream().mapToLong(Long::longValue).toArray());
        
        return result;
    }
    
    /**
     * Build a basic vocabulary for demonstration.
     * In production, load from the model's vocab.txt file.
     */
    private Map<String, Integer> buildBasicVocabulary() {
        Map<String, Integer> vocab = new HashMap<>();
        
        // Common words with sentiment
        String[] positiveWords = {"good", "great", "excellent", "wonderful", "amazing", "happy", 
                                 "love", "best", "perfect", "awesome", "fantastic", "nice"};
        String[] negativeWords = {"bad", "terrible", "awful", "hate", "worst", "sad", "angry",
                                 "horrible", "poor", "disappointed", "frustrating"};
        String[] neutralWords = {"hello", "hi", "hey", "yes", "no", "okay", "thanks", "thank",
                                "you", "bye", "goodbye", "help", "what", "how", "why", "when"};
        
        int id = 1000;
        for (String word : positiveWords) {
            vocab.put(word, id++);
        }
        for (String word : negativeWords) {
            vocab.put(word, id++);
        }
        for (String word : neutralWords) {
            vocab.put(word, id++);
        }
        
        return vocab;
    }
}
