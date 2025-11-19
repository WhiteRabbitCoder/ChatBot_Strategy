# 🔧 ONNX Model Export Guide

This guide explains how to export a sentiment analysis model to ONNX format for use with the ChatBot.

## 📋 Prerequisites

Install the required Python packages:

```bash
pip install transformers onnx onnxruntime torch optimum
```

## 🎯 Recommended Model

**distilbert-base-uncased-finetuned-sst-2-english**

- **Size**: ~268MB
- **Task**: Binary sentiment classification (Positive/Negative)
- **Performance**: Fast on CPU (~50-100ms inference)
- **Accuracy**: ~91% on SST-2 dataset

## 📝 Export Methods

### Method 1: Using Python Script (Recommended)

Create a file `export_model.py`:

```python
from transformers import AutoTokenizer, AutoModelForSequenceClassification
import torch
import onnx

# Model configuration
MODEL_NAME = "distilbert-base-uncased-finetuned-sst-2-english"
OUTPUT_PATH = "sentiment-model.onnx"

print(f"Loading model: {MODEL_NAME}")
model = AutoModelForSequenceClassification.from_pretrained(MODEL_NAME)
tokenizer = AutoTokenizer.from_pretrained(MODEL_NAME)

# Set model to evaluation mode
model.eval()

# Create dummy input
dummy_text = "This is a sample input for model export"
dummy_input = tokenizer(
    dummy_text,
    return_tensors="pt",
    padding="max_length",
    max_length=128,
    truncation=True
)

# Extract input components
input_ids = dummy_input["input_ids"]
attention_mask = dummy_input["attention_mask"]
token_type_ids = dummy_input.get("token_type_ids", torch.zeros_like(input_ids))

print("Exporting model to ONNX...")

# Export to ONNX
torch.onnx.export(
    model,
    (input_ids, attention_mask, token_type_ids),
    OUTPUT_PATH,
    input_names=['input_ids', 'attention_mask', 'token_type_ids'],
    output_names=['logits'],
    dynamic_axes={
        'input_ids': {0: 'batch_size', 1: 'sequence_length'},
        'attention_mask': {0: 'batch_size', 1: 'sequence_length'},
        'token_type_ids': {0: 'batch_size', 1: 'sequence_length'},
        'logits': {0: 'batch_size'}
    },
    opset_version=14,
    do_constant_folding=True
)

print(f"Model exported successfully to: {OUTPUT_PATH}")

# Verify the exported model
print("Verifying exported model...")
onnx_model = onnx.load(OUTPUT_PATH)
onnx.checker.check_model(onnx_model)
print("✓ Model verification passed!")

# Print model info
print("\nModel Information:")
print(f"  - Input names: {[input.name for input in onnx_model.graph.input]}")
print(f"  - Output names: {[output.name for output in onnx_model.graph.output]}")
print(f"  - File size: {os.path.getsize(OUTPUT_PATH) / (1024*1024):.2f} MB")
```

Run the script:

```bash
python export_model.py
```

### Method 2: Using Optimum CLI (Easier)

```bash
optimum-cli export onnx \
    --model distilbert-base-uncased-finetuned-sst-2-english \
    --task sequence-classification \
    sentiment-model/
```

This will create an `sentiment-model/model.onnx` file.

### Method 3: Interactive Python

```python
from transformers import AutoTokenizer, AutoModelForSequenceClassification
import torch

# Load model
model = AutoModelForSequenceClassification.from_pretrained(
    "distilbert-base-uncased-finetuned-sst-2-english"
)
tokenizer = AutoTokenizer.from_pretrained(
    "distilbert-base-uncased-finetuned-sst-2-english"
)

# Prepare dummy input
dummy_input = tokenizer(
    "sample text",
    return_tensors="pt",
    padding="max_length",
    max_length=128
)

# Export
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
    },
    opset_version=14
)
```

## 🧪 Testing the Exported Model

### Using ONNX Runtime (Python)

```python
import onnxruntime as ort
import numpy as np
from transformers import AutoTokenizer

# Load the ONNX model
session = ort.InferenceSession("sentiment-model.onnx")

# Load tokenizer
tokenizer = AutoTokenizer.from_pretrained(
    "distilbert-base-uncased-finetuned-sst-2-english"
)

# Tokenize input
text = "This is great!"
inputs = tokenizer(
    text,
    return_tensors="np",
    padding="max_length",
    max_length=128,
    truncation=True
)

# Run inference
outputs = session.run(
    None,
    {
        "input_ids": inputs["input_ids"].astype(np.int64),
        "attention_mask": inputs["attention_mask"].astype(np.int64),
        "token_type_ids": inputs.get("token_type_ids", np.zeros_like(inputs["input_ids"])).astype(np.int64)
    }
)

# Get prediction
logits = outputs[0]
probabilities = np.exp(logits) / np.sum(np.exp(logits), axis=1, keepdims=True)
prediction = np.argmax(probabilities, axis=1)[0]

labels = ["NEGATIVE", "POSITIVE"]
print(f"Text: {text}")
print(f"Prediction: {labels[prediction]}")
print(f"Confidence: {probabilities[0][prediction]:.2f}")
```

## 🎨 Alternative Lightweight Models

### 1. TinyBERT for Sentiment

```python
MODEL_NAME = "huawei-noah/TinyBERT_General_4L_312D"
# Smaller, faster, slightly less accurate
```

### 2. MobileBERT

```python
MODEL_NAME = "google/mobilebert-uncased"
# Optimized for mobile/edge devices
```

### 3. DistilRoBERTa

```python
MODEL_NAME = "distilroberta-base"
# Alternative to DistilBERT, similar performance
```

## 📊 Model Comparison

| Model | Size | Inference Time (CPU) | Accuracy |
|-------|------|---------------------|----------|
| DistilBERT-SST2 | 268MB | 50-100ms | ~91% |
| TinyBERT | 56MB | 20-40ms | ~85% |
| MobileBERT | 100MB | 40-80ms | ~89% |

## 🔍 Troubleshooting

### Issue: "opset_version not supported"

**Solution**: Use a lower opset version:
```python
opset_version=12  # Instead of 14
```

### Issue: "token_type_ids not found"

**Solution**: Create dummy token_type_ids:
```python
token_type_ids = dummy_input.get("token_type_ids", torch.zeros_like(input_ids))
```

### Issue: Model too large

**Solution**: Use quantization:
```python
from onnxruntime.quantization import quantize_dynamic

quantize_dynamic(
    "sentiment-model.onnx",
    "sentiment-model-quantized.onnx",
    weight_type=QuantType.QUInt8
)
```

### Issue: Slow inference

**Solutions**:
- Use a smaller model (TinyBERT)
- Reduce max_length (e.g., 64 instead of 128)
- Enable ONNX Runtime optimizations in Java

## 🚀 CPU Optimization Tips

1. **Use ONNX Runtime optimization**:
```java
options.setOptimizationLevel(OrtSession.SessionOptions.OptLevel.ALL_OPT);
```

2. **Adjust thread count**:
```java
options.setIntraOpNumThreads(4);  // Match your CPU cores
```

3. **Enable graph optimizations**:
```java
options.setGraphOptimizationLevel(GraphOptimizationLevel.ORT_ENABLE_ALL);
```

## 📚 Resources

- [ONNX Runtime Documentation](https://onnxruntime.ai/)
- [Hugging Face Model Hub](https://huggingface.co/models)
- [ONNX Model Zoo](https://github.com/onnx/models)
- [Optimum Documentation](https://huggingface.co/docs/optimum/index)

## ✅ Verification Checklist

- [ ] Model exports without errors
- [ ] ONNX model passes validation (`onnx.checker.check_model()`)
- [ ] Test inference works in Python
- [ ] Model file size is reasonable (< 500MB)
- [ ] Input/output names match Java code expectations
- [ ] Dynamic axes are configured correctly

## 💡 Next Steps

After exporting your model:

1. Place the `.onnx` file in your project directory
2. Run the chatbot with the model:
   ```bash
   java -jar target/chatbot-strategy-1.0.0.jar sentiment-model.onnx
   ```
3. Test with various inputs to verify sentiment detection
4. Adjust strategy selection thresholds if needed

Happy model exporting! 🎉
