#!/usr/bin/env python3
"""
Export a sentiment analysis model to ONNX format.
This script exports the DistilBERT sentiment analysis model to ONNX for use with the ChatBot.

Requirements:
    pip install transformers torch onnx onnxruntime optimum

Usage:
    python export_sentiment_model.py
"""

import os
import sys

try:
    from transformers import AutoTokenizer, AutoModelForSequenceClassification
    import torch
    import onnx
except ImportError:
    print("Error: Required packages not installed.")
    print("Please install: pip install transformers torch onnx onnxruntime")
    sys.exit(1)

# Configuration
MODEL_NAME = "distilbert-base-uncased-finetuned-sst-2-english"
OUTPUT_PATH = "sentiment-model.onnx"
MAX_LENGTH = 128

def export_model():
    """Export the sentiment analysis model to ONNX format."""
    print(f"🚀 Exporting model: {MODEL_NAME}")
    print(f"📁 Output path: {OUTPUT_PATH}")
    print()
    
    # Load model and tokenizer
    print("Loading model and tokenizer...")
    model = AutoModelForSequenceClassification.from_pretrained(MODEL_NAME)
    tokenizer = AutoTokenizer.from_pretrained(MODEL_NAME)
    
    # Set model to evaluation mode
    model.eval()
    
    # Create dummy input
    print("Creating dummy input...")
    dummy_text = "This is a sample input for model export"
    dummy_input = tokenizer(
        dummy_text,
        return_tensors="pt",
        padding="max_length",
        max_length=MAX_LENGTH,
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
    
    print(f"✅ Model exported successfully to: {OUTPUT_PATH}")
    
    # Verify the exported model
    print("\nVerifying exported model...")
    onnx_model = onnx.load(OUTPUT_PATH)
    onnx.checker.check_model(onnx_model)
    print("✓ Model verification passed!")
    
    # Print model info
    file_size_mb = os.path.getsize(OUTPUT_PATH) / (1024 * 1024)
    print("\n📊 Model Information:")
    print(f"  - Input names: {[input.name for input in onnx_model.graph.input]}")
    print(f"  - Output names: {[output.name for output in onnx_model.graph.output]}")
    print(f"  - File size: {file_size_mb:.2f} MB")
    print()
    print("🎉 Done! You can now use this model with the ChatBot:")
    print(f"   java -jar target/chatbot-strategy-1.0.0.jar {OUTPUT_PATH}")

if __name__ == "__main__":
    try:
        export_model()
    except Exception as e:
        print(f"\n❌ Error during export: {e}")
        import traceback
        traceback.print_exc()
        sys.exit(1)
