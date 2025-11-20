package com.chatbot.services;

import ai.onnxruntime.*;
import com.chatbot.model.Sentiment;
import com.chatbot.utils.SimpleTokenizer;

import java.util.HashMap;
import java.util.Map;

/**
 * Service for detecting mood/sentiment.
 * HYBRID MODE: Rules (Specific personas) + ONNX AI (General sentiment).
 * Minimalist logs & Multiclass support.
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

    private boolean initializeOnnxModel(String modelPath) {
        if (modelPath == null || modelPath.isEmpty()) {
            System.out.println("[Init] Mode: Rule-Based Only");
            return false;
        }
        try {
            env = OrtEnvironment.getEnvironment();
            OrtSession.SessionOptions options = new OrtSession.SessionOptions();
            options.setOptimizationLevel(OrtSession.SessionOptions.OptLevel.BASIC_OPT);
            session = env.createSession(modelPath, options);
            System.out.println("[Init] Mode: AI Hybrid (ONNX Loaded)");
            return true;
        } catch (Exception e) {
            System.err.println("[Init] ⚠️ AI Load Failed. Falling back to Rules.");
            return false;
        }
    }

    public Sentiment detectMood(String text) {
        // 1. Priority: Specific Rule-Based Personas
        Sentiment ruleSentiment = detectMoodRuleBased(text);
        String label = ruleSentiment.getLabel();

        if (isSpecificPersona(label)) {
            logDetection("📜 Rule", label, ruleSentiment.getConfidence());
            return ruleSentiment;
        }

        // 2. AI Detection for General Sentiment
        if (useOnnxModel && session != null) {
            try {
                Sentiment onnxSentiment = detectMoodWithOnnx(text);
                logDetection("🧠 AI", onnxSentiment.getLabel(), onnxSentiment.getConfidence());
                return onnxSentiment;
            } catch (Exception e) {
                // Silent fallback on error
            }
        }

        // 3. Fallback
        logDetection("📜 Rule", label, ruleSentiment.getConfidence());
        return ruleSentiment;
    }

    private boolean isSpecificPersona(String label) {
        return label.equals("CRISIS") || label.equals("ROMANTIC") ||
               label.equals("SAD") || label.equals("NOSTALGIC");
    }

    private void logDetection(String source, String label, float conf) {
        System.out.printf("[%s] %s (%.0f%%)%n", source, label, conf * 100);
    }

    // ---------------------------------------------------------
    // ONNX AI ENGINE
    // ---------------------------------------------------------

    private Sentiment detectMoodWithOnnx(String text) throws OrtException {
        Map<String, long[]> tokens = tokenizer.tokenize(text);

        long[] inputIds = tokens.get("input_ids");
        long[] attentionMask = tokens.get("attention_mask");

        // FIX: Enforce [1][SequenceLength] dimensions
        int seqLen = inputIds.length;
        long[][] inputIds2D = new long[1][seqLen];
        long[][] attnMask2D = new long[1][seqLen];

        System.arraycopy(inputIds, 0, inputIds2D[0], 0, seqLen);
        System.arraycopy(attentionMask, 0, attnMask2D[0], 0, seqLen);

        Map<String, OnnxTensor> inputs = new HashMap<>();
        inputs.put("input_ids", OnnxTensor.createTensor(env, inputIds2D));
        inputs.put("attention_mask", OnnxTensor.createTensor(env, attnMask2D));
        // Note: 'token_type_ids' omitted for compatibility with DistilBERT/RoBERTa

        try (OrtSession.Result results = session.run(inputs)) {
            float[][] logits = (float[][]) results.get(0).getValue();
            float[] probs = softmax(logits[0]);
            int predictedClass = argmax(probs);

            String label;
            if (probs.length == 3) {
                // 3-Class Model (Twitter-RoBERTa): 0=Neg, 1=Neu, 2=Pos
                if (predictedClass == 0) label = "NEGATIVE";
                else if (predictedClass == 1) label = "NEUTRAL";
                else label = "POSITIVE";
            } else {
                // 2-Class Model (SST-2): 0=Neg, 1=Pos
                label = (predictedClass == 1) ? "POSITIVE" : "NEGATIVE";
            }

            return new Sentiment(label, probs[predictedClass]);
        }
    }

    // ---------------------------------------------------------
    // RULE ENGINE
    // ---------------------------------------------------------

    private Sentiment detectMoodRuleBased(String text) {
        String lower = text.toLowerCase();

        // 1. CRISIS
        String[] crisis = {"kill myself", "suicide", "want to die", "better off dead", "self-harm"};
        if (containsAny(lower, crisis)) return new Sentiment("CRISIS", 1.0f);

        // 2. SPECIFIC EMOTIONS
        String[] romantic = {"love", "sweetheart", "darling", "soulmate", "kiss", "marry", "romance"};
        if (containsAny(lower, romantic)) return new Sentiment("ROMANTIC", 0.9f);

        String[] sad = {"sad", "crying", "tears", "depressed", "heartbroken", "grief", "lonely", "blue"};
        if (containsAny(lower, sad)) return new Sentiment("SAD", 0.9f);

        String[] nostalgic = {"remember", "memories", "used to be", "childhood", "old days", "nostalgia"};
        if (containsAny(lower, nostalgic)) return new Sentiment("NOSTALGIC", 0.8f);

        // 3. GENERAL (Fallback)
        String[] pos = {"good", "great", "happy", "awesome", "nice", "cool", "thanks"};
        String[] neg = {"bad", "terrible", "awful", "hate", "angry", "mad", "stupid"};

        int p = countMatches(lower, pos);
        int n = countMatches(lower, neg);

        if (p > n) return new Sentiment("POSITIVE", 0.7f);
        if (n > p) return new Sentiment("NEGATIVE", 0.7f);
        return new Sentiment("NEUTRAL", 0.5f);
    }

    // ---------------------------------------------------------
    // UTILS
    // ---------------------------------------------------------

    private boolean containsAny(String text, String[] keywords) {
        for (String k : keywords) if (text.contains(k)) return true;
        return false;
    }

    private int countMatches(String text, String[] keywords) {
        int c = 0;
        for (String k : keywords) if (text.contains(k)) c++;
        return c;
    }

    private float[] softmax(float[] logits) {
        float max = Float.NEGATIVE_INFINITY;
        for (float val : logits) if (val > max) max = val;

        float sum = 0.0f;
        float[] exp = new float[logits.length];
        for (int i = 0; i < logits.length; i++) {
            exp[i] = (float) Math.exp(logits[i] - max);
            sum += exp[i];
        }
        for (int i = 0; i < exp.length; i++) exp[i] /= sum;
        return exp;
    }

    private int argmax(float[] array) {
        int maxIdx = 0;
        for (int i = 1; i < array.length; i++) if (array[i] > array[maxIdx]) maxIdx = i;
        return maxIdx;
    }

    public void close() {
        try { if (session != null) session.close(); if (env != null) env.close(); } catch (Exception e) {}
    }
}