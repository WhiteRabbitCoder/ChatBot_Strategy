package com.chatbot.services;

import ai.onnxruntime.*;
import com.chatbot.model.Sentiment;
import com.chatbot.utils.SimpleTokenizer;

import java.nio.LongBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * Service for detecting mood/sentiment using ONNX Runtime.
 * Optimized for CPU execution without GPU.
 */
public class MoodDetectionService {
    private OrtEnvironment env;
    private OrtSession session;
    private final SimpleTokenizer tokenizer;
    private final boolean useOnnxModel;
    
    public MoodDetectionService(String modelPath) {
        this.tokenizer = new SimpleTokenizer();
        this.useOnnxModel = initializeOnnxModel(modelPath);
    }
    
    /**
     * Initialize ONNX model if available.
     * Falls back to rule-based detection if model is not found.
     */
    private boolean initializeOnnxModel(String modelPath) {
        if (modelPath == null || modelPath.isEmpty()) {
            System.out.println("[MoodDetection] No model path provided. Using rule-based sentiment analysis.");
            return false;
        }
        
        try {
            env = OrtEnvironment.getEnvironment();
            
            // Create session options for CPU execution
            OrtSession.SessionOptions options = new OrtSession.SessionOptions();
            options.setOptimizationLevel(OrtSession.SessionOptions.OptLevel.BASIC_OPT);
            options.setInterOpNumThreads(2);
            options.setIntraOpNumThreads(2);
            
            session = env.createSession(modelPath, options);
            System.out.println("[MoodDetection] ONNX model loaded successfully from: " + modelPath);
            System.out.println("[MoodDetection] Model inputs: " + session.getInputNames());
            System.out.println("[MoodDetection] Model outputs: " + session.getOutputNames());
            return true;
        } catch (Exception e) {
            System.err.println("[MoodDetection] Failed to load ONNX model: " + e.getMessage());
            System.out.println("[MoodDetection] Falling back to rule-based sentiment analysis.");
            return false;
        }
    }
    
    /**
     * Detect sentiment from user input.
     * Uses ONNX model if available, otherwise falls back to rule-based analysis.
     */
    public Sentiment detectMood(String text) {
        if (useOnnxModel && session != null) {
            return detectMoodWithOnnx(text);
        } else {
            return detectMoodRuleBased(text);
        }
    }
    
    /**
     * ONNX-based sentiment detection.
     */
    private Sentiment detectMoodWithOnnx(String text) {
        try {
            // Tokenize input
            Map<String, long[]> tokens = tokenizer.tokenize(text);
            
            // Prepare ONNX inputs
            Map<String, OnnxTensor> inputs = new HashMap<>();
            
            long[] inputIds = tokens.get("input_ids");
            long[] attentionMask = tokens.get("attention_mask");
            long[] tokenTypeIds = tokens.get("token_type_ids");
            
            long[][] inputIdsArray = new long[1][inputIds.length];
            long[][] attentionMaskArray = new long[1][attentionMask.length];
            long[][] tokenTypeIdsArray = new long[1][tokenTypeIds.length];
            
            inputIdsArray[0] = inputIds;
            attentionMaskArray[0] = attentionMask;
            tokenTypeIdsArray[0] = tokenTypeIds;
            
            inputs.put("input_ids", OnnxTensor.createTensor(env, inputIdsArray));
            inputs.put("attention_mask", OnnxTensor.createTensor(env, attentionMaskArray));
            inputs.put("token_type_ids", OnnxTensor.createTensor(env, tokenTypeIdsArray));
            
            // Run inference
            try (OrtSession.Result results = session.run(inputs)) {
                // Get logits output
                float[][] logits = (float[][]) results.get(0).getValue();
                
                // Apply softmax and get prediction
                float[] probabilities = softmax(logits[0]);
                int predictedClass = argmax(probabilities);
                
                String label = predictedClass == 1 ? "POSITIVE" : "NEGATIVE";
                float confidence = probabilities[predictedClass];
                
                return new Sentiment(label, confidence);
            }
        } catch (Exception e) {
            System.err.println("[MoodDetection] Error during ONNX inference: " + e.getMessage());
            return detectMoodRuleBased(text);
        }
    }
    
    /**
     * Rule-based sentiment detection as fallback.
     * Simple keyword matching for demonstration.
     */
    private Sentiment detectMoodRuleBased(String text) {
        String lowerText = text.toLowerCase();
        
        // Positive keywords
        String[] positiveKeywords = {"good", "great", "excellent", "wonderful", "amazing", 
                                     "happy", "love", "best", "perfect", "awesome", "fantastic", 
                                     "nice", "thank", "thanks"};
        
        // Negative keywords
        String[] negativeKeywords = {"bad", "terrible", "awful", "hate", "worst", "sad", 
                                     "angry", "horrible", "poor", "disappointed", "frustrating"};
        
        int positiveCount = 0;
        int negativeCount = 0;
        
        for (String keyword : positiveKeywords) {
            if (lowerText.contains(keyword)) {
                positiveCount++;
            }
        }
        
        for (String keyword : negativeKeywords) {
            if (lowerText.contains(keyword)) {
                negativeCount++;
            }
        }
        
        if (positiveCount > negativeCount) {
            float confidence = Math.min(0.7f + (positiveCount * 0.1f), 0.95f);
            return new Sentiment("POSITIVE", confidence);
        } else if (negativeCount > positiveCount) {
            float confidence = Math.min(0.7f + (negativeCount * 0.1f), 0.95f);
            return new Sentiment("NEGATIVE", confidence);
        } else {
            return new Sentiment("NEUTRAL", 0.6f);
        }
    }
    
    /**
     * Apply softmax to convert logits to probabilities.
     */
    private float[] softmax(float[] logits) {
        float max = Float.NEGATIVE_INFINITY;
        for (float logit : logits) {
            if (logit > max) max = logit;
        }
        
        float sum = 0.0f;
        float[] exp = new float[logits.length];
        for (int i = 0; i < logits.length; i++) {
            exp[i] = (float) Math.exp(logits[i] - max);
            sum += exp[i];
        }
        
        for (int i = 0; i < exp.length; i++) {
            exp[i] /= sum;
        }
        
        return exp;
    }
    
    /**
     * Find index of maximum value.
     */
    private int argmax(float[] array) {
        int maxIndex = 0;
        float maxValue = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > maxValue) {
                maxValue = array[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }
    
    /**
     * Clean up resources.
     */
    public void close() {
        try {
            if (session != null) {
                session.close();
            }
            if (env != null) {
                env.close();
            }
        } catch (Exception e) {
            System.err.println("Error closing ONNX resources: " + e.getMessage());
        }
    }
}
