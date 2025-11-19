# 🧠 ChatBot with Strategy Pattern + NLP ONNX in Java

A conversational chatbot that uses the **Strategy Pattern** to dynamically change its response behavior based on the user's detected mood. The project uses ONNX Runtime for CPU-optimized sentiment analysis.

## 📋 Features

- **Mood Detection**: Uses ONNX Runtime with a lightweight sentiment analysis model (CPU-optimized)
- **Strategy Pattern**: Dynamically selects response strategies based on detected sentiment
- **Conversational**: Maintains conversation history for more natural interactions
- **Extensible**: Easy to add new strategies (e.g., `AngryStrategy`, `EmpatheticStrategy`)
- **CPU-Optimized**: Works on laptops without GPU (tested on Ryzen 5 5500U, 32GB RAM)

## 🏗️ Architecture

```
┌─────────────────┐
│   User Input    │
└────────┬────────┘
         │
         ▼
┌─────────────────────────┐
│ MoodDetectionService    │
│ (ONNX Runtime)          │
└────────┬────────────────┘
         │ Sentiment
         ▼
┌─────────────────────────┐
│  StrategySelector       │
└────────┬────────────────┘
         │ Selected Strategy
         ▼
┌─────────────────────────┐
│  ResponseStrategy       │◄──── NeutralStrategy
│  (interface)            │◄──── FriendlyStrategy
│                         │◄──── HumorousStrategy
└────────┬────────────────┘
         │ Response
         ▼
┌─────────────────────────┐
│  ChatBot Orchestrator   │
│  (with history)         │
└─────────────────────────┘
```

## 📁 Project Structure

```
ChatBot_Strategy/
├── pom.xml                                 # Maven configuration
├── README.md                               # This file
├── ONNX_MODEL_GUIDE.md                     # Guide for exporting ONNX models
└── src/
    └── main/
        └── java/
            └── com/
                └── chatbot/
                    ├── ChatBot.java                    # Main orchestrator
                    ├── model/
                    │   └── Sentiment.java              # Sentiment model
                    ├── services/
                    │   └── MoodDetectionService.java   # ONNX inference
                    ├── strategies/
                    │   ├── ResponseStrategy.java       # Strategy interface
                    │   ├── NeutralStrategy.java        # Neutral responses
                    │   ├── FriendlyStrategy.java       # Friendly responses
                    │   ├── HumorousStrategy.java       # Humorous responses
                    │   └── StrategySelector.java       # Strategy selection
                    └── utils/
                        └── SimpleTokenizer.java        # Text tokenization
```

## 🚀 Getting Started

### Prerequisites

- **Java 17** or higher
- **Maven 3.6+**
- **32GB RAM** recommended (works with less)
- **CPU-only** (no GPU required)

### Installation

1. **Clone the repository**:
```bash
git clone https://github.com/WhiteRabbitCoder/ChatBot_Strategy.git
cd ChatBot_Strategy
```

2. **Build the project**:
```bash
mvn clean package
```

This will:
- Download dependencies (ONNX Runtime, Gson)
- Compile the code
- Run tests
- Create an executable JAR with dependencies

### Running the ChatBot

#### Option 1: Without ONNX Model (Rule-based)

```bash
java -jar target/chatbot-strategy-1.0.0.jar
```

The chatbot will use rule-based sentiment analysis (keyword matching).

#### Option 2: With ONNX Model

```bash
java -jar target/chatbot-strategy-1.0.0.jar path/to/sentiment-model.onnx
```

The chatbot will use ONNX Runtime for sentiment analysis.

## 🎯 Usage Example

```
╔════════════════════════════════════════════════════════════╗
║   ChatBot with Strategy Pattern + ONNX NLP                ║
║   CPU-Optimized Sentiment Analysis                        ║
╚════════════════════════════════════════════════════════════╝

ChatBot is ready! Type your messages below.
Type 'exit', 'quit', or 'bye' to end the conversation.
─────────────────────────────────────────────────────────────

You: Hello!
[Debug] Detected sentiment: Sentiment{label='NEUTRAL', confidence=0.60}
[Debug] Selected strategy: NeutralStrategy
Bot: Hello. How can I assist you today?

You: I'm having a great day!
[Debug] Detected sentiment: Sentiment{label='POSITIVE', confidence=0.80}
[Debug] Selected strategy: FriendlyStrategy
Bot: That's fantastic! I'm thrilled to hear that! Keep up the great work!

You: This is amazing!
[Debug] Detected sentiment: Sentiment{label='POSITIVE', confidence=0.80}
[Debug] Selected strategy: HumorousStrategy
Bot: Ha! That's funny! Why do programmers prefer dark mode? Because light attracts bugs! 🐛 Anyway, what else is on your mind?

You: bye
[Debug] Detected sentiment: Sentiment{label='NEUTRAL', confidence=0.60}
[Debug] Selected strategy: NeutralStrategy
Bot: Goodbye. Feel free to return if you need assistance.

Thank you for chatting! Goodbye! 👋
```

## 🤖 ONNX Model Setup

### Recommended Model

**distilbert-base-uncased-finetuned-sst-2-english** - A lightweight sentiment analysis model perfect for CPU inference.

### Quick Export Guide

See [ONNX_MODEL_GUIDE.md](ONNX_MODEL_GUIDE.md) for detailed instructions on exporting models to ONNX format.

**Quick steps**:

1. Install required Python packages:
```bash
pip install transformers onnx onnxruntime torch
```

2. Export the model:
```python
from transformers import AutoTokenizer, AutoModelForSequenceClassification
import torch

model_name = "distilbert-base-uncased-finetuned-sst-2-english"
model = AutoModelForSequenceClassification.from_pretrained(model_name)
tokenizer = AutoTokenizer.from_pretrained(model_name)

dummy_input = tokenizer("This is a sample", return_tensors="pt")
torch.onnx.export(
    model,
    tuple(dummy_input.values()),
    "sentiment-model.onnx",
    input_names=['input_ids', 'attention_mask', 'token_type_ids'],
    output_names=['logits'],
    dynamic_axes={
        'input_ids': {0: 'batch', 1: 'sequence'},
        'attention_mask': {0: 'batch', 1: 'sequence'},
        'token_type_ids': {0: 'batch', 1: 'sequence'}
    }
)
```

3. Use the exported model:
```bash
java -jar target/chatbot-strategy-1.0.0.jar sentiment-model.onnx
```

## 🔧 Extending the ChatBot

### Adding a New Strategy

1. Create a new strategy class implementing `ResponseStrategy`:

```java
package com.chatbot.strategies;

public class EmpatheticStrategy implements ResponseStrategy {
    @Override
    public String generateResponse(String userInput, String conversationHistory) {
        return "I understand how you feel. Let's talk about it.";
    }
    
    @Override
    public String getStrategyName() {
        return "EmpatheticStrategy";
    }
}
```

2. Add it to `StrategySelector`:

```java
private final ResponseStrategy empatheticStrategy = new EmpatheticStrategy();

public ResponseStrategy selectStrategy(Sentiment sentiment) {
    if (sentiment.isNegative() && sentiment.getConfidence() > 0.7f) {
        return empatheticStrategy; // Use empathetic for strong negative sentiment
    }
    // ... rest of the logic
}
```

## 📊 Performance

- **Model Loading**: < 2 seconds
- **Inference Time**: ~50-100ms per message (CPU)
- **Memory Usage**: ~500MB with ONNX model loaded
- **Tested on**: Ryzen 5 5500U, 32GB RAM (no GPU)

## 🛠️ Technologies Used

- **Java 17**: Modern Java features
- **Maven**: Build and dependency management
- **ONNX Runtime 1.16.3**: CPU-optimized ML inference
- **Gson**: JSON processing
- **Strategy Pattern**: Clean, extensible architecture

## 📝 Design Patterns

This project strictly uses the **Strategy Pattern** for response generation:

- **Context**: `ChatBot` orchestrator
- **Strategy Interface**: `ResponseStrategy`
- **Concrete Strategies**: `NeutralStrategy`, `FriendlyStrategy`, `HumorousStrategy`
- **Strategy Selector**: `StrategySelector` (chooses the right strategy based on sentiment)

## 🧪 Testing

Run tests with:
```bash
mvn test
```

## 📄 License

This project is open source and available under the MIT License.

## 🤝 Contributing

Contributions are welcome! Feel free to:
- Add new strategies
- Improve sentiment detection
- Optimize performance
- Add more conversational rules

## 📧 Contact

For questions or suggestions, please open an issue on GitHub.