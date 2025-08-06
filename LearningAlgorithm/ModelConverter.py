import torch
from torchvision import datasets, transforms, models
import os

# Path to your dataset and model weights
DATA_DIR = os.path.join(os.path.dirname(__file__), '../Database')
MODEL_WEIGHTS = os.path.join(os.path.dirname(__file__), '../Models/ModelWeights/plant_classifier_efficientnetb0.pth')
ANDROID_MODEL_DIR = os.path.join(os.path.dirname(__file__), '../Models/AndroidModel')
DEVICE = torch.device("cuda" if torch.cuda.is_available() else "cpu")

# Minimal transform for dataset loading
transform = transforms.Compose([transforms.ToTensor()])

# Load the training dataset to get class names/count
trainDataset = datasets.ImageFolder(os.path.join(DATA_DIR, 'train'), transform=transform)
NUM_CLASSES = len(trainDataset.classes)
print(f"Detected {NUM_CLASSES} classes: {trainDataset.classes}")

# Model definition (must match your training code)
model = models.efficientnet_b0(pretrained=False)
model.classifier[1] = torch.nn.Linear(model.classifier[1].in_features, NUM_CLASSES)

# Load trained weights
model.load_state_dict(torch.load(MODEL_WEIGHTS, map_location=DEVICE))
model.eval()

# Example input for tracing (batch size 1, 3 channels, 224x224)
exampleInput = torch.rand(1, 3, 224, 224)

# Convert to TorchScript (for Android)
os.makedirs(ANDROID_MODEL_DIR, exist_ok=True)
tracedScriptModule = torch.jit.trace(model, exampleInput)
tracedScriptModule.save(os.path.join(ANDROID_MODEL_DIR,'plant_classifier_efficientnetb0.pt'))

print("TorchScript model saved as ../Models/AndroidModel/plant_classifier_efficientnetb0.pt")