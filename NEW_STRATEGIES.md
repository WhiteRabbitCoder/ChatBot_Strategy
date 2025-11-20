# 🆕 New Strategies Documentation

This document describes the 4 new strategies added to the ChatBot: **Romantic**, **Sad**, **Nostalgic**, and **Extreme Support**.

## Overview

The chatbot now supports **7 total strategies**:

| Strategy | Emotion/Situation | Priority | Purpose |
|----------|------------------|----------|---------|
| **ExtremeSupportStrategy** | Crisis/Danger | 🔴 HIGHEST | Life-threatening situations |
| **RomanticStrategy** | Love/Romance | High | Romantic expressions |
| **SadStrategy** | Sadness/Depression | High | Emotional support |
| **NostalgicStrategy** | Nostalgia/Memories | High | Reflective conversations |
| **FriendlyStrategy** | Positive | Medium | Warm encouragement |
| **HumorousStrategy** | Positive | Medium | Playful humor |
| **NeutralStrategy** | Neutral/Default | Low | Balanced responses |

## Strategy Details

### 1. 💕 RomanticStrategy

**Purpose:** Responds to romantic expressions with warmth and affection.

**Triggered by keywords:**
- love, amor, te amo, in love, my love
- heart, corazón, my heart
- kiss, beso
- beautiful, hermoso, beautiful soul
- soulmate, forever, darling, sweetheart

**Example responses:**
```
User: I love you so much
Bot: How lovely! Love is such a wonderful feeling. It makes everything 
     brighter! 💕

User: You have my heart
Bot: That's beautiful! Matters of the heart are the most beautiful. 
     Tell me more about what's in yours! 💖
```

**Response style:**
- Warm and affectionate
- Uses heart emojis (❤️, 💕, 💗, 💖, 💝)
- Celebrates love and connection
- Encourages sharing feelings

---

### 2. 😢 SadStrategy

**Purpose:** Provides empathetic support for sadness and grief.

**Triggered by keywords:**
- depressed, depression
- crying, tears, can't stop crying
- heartbroken, broken heart
- devastated, miserable, hopeless
- lonely, empty inside
- grief, so sad

**Example responses:**
```
User: I'm feeling really depressed and crying
Bot: I'm sorry to hear that. It's completely okay to cry. Tears can be 
     healing. Would you like to talk about what's bothering you?

User: I feel so alone
Bot: That must be difficult. Loneliness can be so heavy. Please remember 
     you're not truly alone. I'm here, and there are people who care about you.
```

**Response style:**
- Empathetic prefixes ("I'm sorry to hear that", "That must be difficult")
- Validates feelings ("Your feelings are valid", "It's okay to feel sad")
- Offers support ("I'm here to listen", "You're not alone")
- Patient and understanding tone

---

### 3. 🕰️ NostalgicStrategy

**Purpose:** Engages with memories and reflections on the past.

**Triggered by keywords:**
- remember when, recall
- used to, back then, before
- childhood, when I was young
- the good old days, old days
- miss the old, wish I could go back
- nostalgia, reminds me, looking back

**Example responses:**
```
User: I remember when I was a child
Bot: Ah, memories... Those early years hold a special magic, don't they? 
     The simplicity and wonder of youth stays with us forever.

User: I wish I could go back to those days
Bot: Those were the days... Longing for the past is natural. Those 
     experiences are part of your story. What would you like to recreate?
```

**Response style:**
- Reflective prefixes ("Ah, memories...", "Looking back...", "How time flies...")
- Honors the past while embracing the present
- Encourages sharing memories
- Gentle and contemplative tone

---

### 4. 🚨 ExtremeSupportStrategy (CRISIS)

**Purpose:** **HIGHEST PRIORITY** - Provides immediate crisis intervention and resources for life-threatening situations.

**Triggered by keywords (self-harm):**
- kill myself, end my life, want to die
- suicide, suicidal
- better off dead, no reason to live
- ending it all, can't go on
- don't want to live, take my life

**Triggered by keywords (harm to others):**
- kill them, hurt them, harm others
- shoot up, attack, going to hurt
- make them pay, revenge
- kill everyone, murder, going to kill

**Example response:**
```
User: I want to end my life
Bot: 🚨 I'm very concerned about what you've shared. Your safety is the 
     most important thing right now.

     Please know that you don't have to face this alone. There are people 
     who want to help you right now:

     🆘 CRISIS RESOURCES:
     • National Suicide Prevention Lifeline: 988 (US)
     • Crisis Text Line: Text HOME to 741741
     • International Association for Suicide Prevention: 
       https://www.iasp.info/resources/Crisis_Centres/
     • Emergency Services: 911 (US) or your local emergency number

     Your life has value. These feelings can be overwhelming, but they 
     are temporary. Please reach out to one of these resources immediately. 
     They are available 24/7 and want to help you.

     💙 You matter. Your life matters. Please reach out for help.
```

**Response features:**
- **Urgent visual indicators:** 🚨, 🆘, 💙
- **Confidence:** 99% (highest priority)
- **Immediate resources:** Crisis hotlines, text lines, emergency services
- **International support:** Links to global resources
- **Validation:** "Your life matters", "You're not alone"
- **Clear action items:** Specific phone numbers and contacts

---

## Strategy Selection Logic

The chatbot uses a **priority-based selection** system:

```
1. CRISIS detected (99% confidence)
   ↓
   ExtremeSupportStrategy ← ALWAYS SELECTED (Safety first!)

2. ROMANTIC detected (75%+ confidence)
   ↓
   RomanticStrategy

3. SAD detected (75%+ confidence)
   ↓
   SadStrategy

4. NOSTALGIC detected (75%+ confidence)
   ↓
   NostalgicStrategy

5. POSITIVE detected (60%+ confidence)
   ↓
   FriendlyStrategy OR HumorousStrategy (random)

6. NEGATIVE detected (50%+ confidence)
   ↓
   NeutralStrategy

7. Default (all other cases)
   ↓
   NeutralStrategy
```

## Technical Implementation

### Sentiment Model Updates

New sentiment labels added:
```java
public boolean isRomantic() {
    return "ROMANTIC".equalsIgnoreCase(label);
}

public boolean isSad() {
    return "SAD".equalsIgnoreCase(label);
}

public boolean isNostalgic() {
    return "NOSTALGIC".equalsIgnoreCase(label);
}

public boolean isCrisis() {
    return "CRISIS".equalsIgnoreCase(label);
}
```

### MoodDetectionService Enhancement

**Priority order:**
1. **Crisis detection** (checked FIRST - highest priority)
2. **Specific emotions** (romantic, sad, nostalgic)
3. **General sentiment** (positive, negative, neutral)

### Testing

All strategies have comprehensive tests:
```bash
mvn test
# Results: 19 tests, all passing
```

## Usage Examples

### Real Conversation Demo

```
╔════════════════════════════════════════════════════════════╗
║   ChatBot with Strategy Pattern + ONNX NLP                ║
║   CPU-Optimized Sentiment Analysis                        ║
╚════════════════════════════════════════════════════════════╝

You: I love you so much
[Debug] Detected sentiment: Sentiment{label='ROMANTIC', confidence=0.85}
[Debug] Selected strategy: RomanticStrategy
Bot: How lovely! How sweet! There's nothing quite like the feeling 
     of being in love.

You: I'm feeling really depressed and crying
[Debug] Detected sentiment: Sentiment{label='SAD', confidence=0.95}
[Debug] Selected strategy: SadStrategy
Bot: I'm sorry to hear that. It's completely okay to cry. Tears can 
     be healing. Would you like to talk about what's bothering you?

You: I remember when I was a child
[Debug] Detected sentiment: Sentiment{label='NOSTALGIC', confidence=0.85}
[Debug] Selected strategy: NostalgicStrategy
Bot: Looking back... Memories can be bittersweet treasures. What makes 
     this memory special to you?

You: I want to end my life
[Debug] Detected sentiment: Sentiment{label='CRISIS', confidence=0.99}
[Debug] Selected strategy: ExtremeSupportStrategy
Bot: 🚨 I'm very concerned about what you've shared. Your safety is the 
     most important thing right now.
     
     [Crisis resources provided...]
```

## Extensibility

Adding new strategies is easy! Just:

1. **Create the strategy class:**
```java
public class MyNewStrategy implements ResponseStrategy {
    @Override
    public String generateResponse(String userInput, String conversationHistory) {
        return "My custom response";
    }
    
    @Override
    public String getStrategyName() {
        return "MyNewStrategy";
    }
}
```

2. **Add to StrategySelector:**
```java
private final ResponseStrategy myNewStrategy = new MyNewStrategy();

public ResponseStrategy selectStrategy(Sentiment sentiment) {
    if (sentiment.isMyNewEmotion()) {
        return myNewStrategy;
    }
    // ... rest of logic
}
```

3. **Update MoodDetectionService** to detect the new emotion
4. **Add tests** for the new strategy

## Safety & Ethics

### Crisis Response Design Principles

1. **Safety First:** Crisis detection has highest priority (99% confidence)
2. **Immediate Action:** Provides hotlines and emergency contacts
3. **Non-judgmental:** Validates feelings without criticism
4. **Professional Help:** Directs to trained crisis counselors
5. **International:** Includes global resources
6. **Clear & Direct:** No ambiguity in crisis situations

### Important Notes

⚠️ **This chatbot is NOT a replacement for professional mental health services**

✅ **What it does:**
- Provides empathetic responses
- Offers crisis hotline information
- Validates emotions
- Encourages seeking help

❌ **What it does NOT do:**
- Provide therapy or medical advice
- Replace emergency services
- Diagnose mental health conditions
- Store or track conversations

## Summary

The addition of these 4 new strategies makes the chatbot:
- 🎯 **More versatile** - Handles 7 different emotional states
- ❤️ **More empathetic** - Responds appropriately to sadness and love
- 🕰️ **More thoughtful** - Engages with memories and nostalgia
- 🚨 **Safer** - Prioritizes life-threatening situations with crisis resources

Total strategies: **7**
Total tests: **19** (all passing)
Performance: **~50-100ms** per response (CPU)

---

*For more information, see:*
- [README.md](README.md) - Main documentation
- [EXECUTION_EXAMPLE.md](EXECUTION_EXAMPLE.md) - Usage examples
- [PROJECT_SUMMARY.md](PROJECT_SUMMARY.md) - Project overview
