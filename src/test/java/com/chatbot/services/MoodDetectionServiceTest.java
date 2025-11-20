package com.chatbot.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MoodDetectionServiceTest {
    
    private MoodDetectionService moodDetectionService;
    
    @BeforeEach
    public void setUp() {
        moodDetectionService = new MoodDetectionService();
    }
    
    @Test
    public void testNullInput() {
        assertEquals("NEUTRAL", moodDetectionService.detectMoodRuleBased(null));
    }
    
    @Test
    public void testEmptyInput() {
        assertEquals("NEUTRAL", moodDetectionService.detectMoodRuleBased(""));
        assertEquals("NEUTRAL", moodDetectionService.detectMoodRuleBased("   "));
    }
    
    // Tests for SAD mood detection
    @Test
    public void testSadMood_ExistingKeywords() {
        assertEquals("SAD", moodDetectionService.detectMoodRuleBased("I'm so sad today"));
        assertEquals("SAD", moodDetectionService.detectMoodRuleBased("Feeling depressed"));
        assertEquals("SAD", moodDetectionService.detectMoodRuleBased("I am miserable"));
    }
    
    @Test
    public void testSadMood_NewKeywords() {
        // These should be SAD after adding the new keywords
        assertEquals("SAD", moodDetectionService.detectMoodRuleBased("I'm sad"));
        assertEquals("SAD", moodDetectionService.detectMoodRuleBased("Feeling unhappy"));
        assertEquals("SAD", moodDetectionService.detectMoodRuleBased("I'm down today"));
        assertEquals("SAD", moodDetectionService.detectMoodRuleBased("Feeling blue"));
        assertEquals("SAD", moodDetectionService.detectMoodRuleBased("It's so gloomy"));
        assertEquals("SAD", moodDetectionService.detectMoodRuleBased("Full of sorrow"));
        assertEquals("SAD", moodDetectionService.detectMoodRuleBased("In a melancholy mood"));
    }
    
    // Tests for NEGATIVE mood detection
    @Test
    public void testNegativeMood_ExistingKeywords() {
        assertEquals("NEGATIVE", moodDetectionService.detectMoodRuleBased("I'm angry"));
        assertEquals("NEGATIVE", moodDetectionService.detectMoodRuleBased("This is terrible"));
        assertEquals("NEGATIVE", moodDetectionService.detectMoodRuleBased("I hate this"));
    }
    
    @Test
    public void testNegativeMood_NewKeywords() {
        // These should be NEGATIVE after adding the new keywords
        assertEquals("NEGATIVE", moodDetectionService.detectMoodRuleBased("I'm mad"));
        assertEquals("NEGATIVE", moodDetectionService.detectMoodRuleBased("I'm upset"));
        assertEquals("NEGATIVE", moodDetectionService.detectMoodRuleBased("So annoyed"));
        assertEquals("NEGATIVE", moodDetectionService.detectMoodRuleBased("I'm bored"));
        assertEquals("NEGATIVE", moodDetectionService.detectMoodRuleBased("I'm tired"));
        assertEquals("NEGATIVE", moodDetectionService.detectMoodRuleBased("I'm sick of this"));
        assertEquals("NEGATIVE", moodDetectionService.detectMoodRuleBased("In pain"));
        assertEquals("NEGATIVE", moodDetectionService.detectMoodRuleBased("That hurt"));
        assertEquals("NEGATIVE", moodDetectionService.detectMoodRuleBased("That's stupid"));
        assertEquals("NEGATIVE", moodDetectionService.detectMoodRuleBased("You're an idiot"));
    }
    
    // Tests for POSITIVE mood detection
    @Test
    public void testPositiveMood_ExistingKeywords() {
        assertEquals("POSITIVE", moodDetectionService.detectMoodRuleBased("I'm happy"));
        assertEquals("POSITIVE", moodDetectionService.detectMoodRuleBased("This is great"));
        assertEquals("POSITIVE", moodDetectionService.detectMoodRuleBased("Feeling wonderful"));
    }
    
    @Test
    public void testPositiveMood_NewKeywords() {
        // These should be POSITIVE after adding the new keywords
        assertEquals("POSITIVE", moodDetectionService.detectMoodRuleBased("I'm glad"));
        assertEquals("POSITIVE", moodDetectionService.detectMoodRuleBased("That's cool"));
        assertEquals("POSITIVE", moodDetectionService.detectMoodRuleBased("Yay!"));
        assertEquals("POSITIVE", moodDetectionService.detectMoodRuleBased("I'm excited"));
        assertEquals("POSITIVE", moodDetectionService.detectMoodRuleBased("This is fun"));
        assertEquals("POSITIVE", moodDetectionService.detectMoodRuleBased("I enjoy this"));
    }
    
    // Test for ROMANTIC mood (love should be detected as ROMANTIC, not POSITIVE)
    @Test
    public void testRomanticMood() {
        assertEquals("ROMANTIC", moodDetectionService.detectMoodRuleBased("I love you"));
        assertEquals("ROMANTIC", moodDetectionService.detectMoodRuleBased("My crush is amazing"));
    }
    
    // Test that "love" in POSITIVE context is still ROMANTIC (priority)
    @Test
    public void testLoveAsPriority() {
        // "love" should be caught by romantic keywords first
        assertEquals("ROMANTIC", moodDetectionService.detectMoodRuleBased("I would love to help"));
    }
    
    @Test
    public void testNeutralMood() {
        assertEquals("NEUTRAL", moodDetectionService.detectMoodRuleBased("Hello there"));
        assertEquals("NEUTRAL", moodDetectionService.detectMoodRuleBased("What's the weather?"));
        assertEquals("NEUTRAL", moodDetectionService.detectMoodRuleBased("Tell me a fact"));
    }
}
