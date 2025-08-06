import torch
from torchvision import datasets, transforms, models
import os

# Base directories relative to current file
DATA_DIR = os.path.join(os.path.dirname(__file__), '../Database')
MODEL_WEIGHTS = os.path.join(os.path.dirname(__file__), '../Models/ModelWeights/plant_classifier_efficientnetb0.pth')
ANDROID_MODEL_PATH = os.path.join(os.path.dirname(__file__), '../Application/app/src/main/assets/plant_classifier_efficientnetb0.pt')

# Ensure the assets directory exists
os.makedirs(os.path.dirname(ANDROID_MODEL_PATH), exist_ok=True)

DEVICE = torch.device("cuda" if torch.cuda.is_available() else "cpu")

transform = transforms.Compose([transforms.ToTensor()])

trainDataset = datasets.ImageFolder(os.path.join(DATA_DIR, 'train'), transform=transform)
NUM_CLASSES = len(trainDataset.classes)
print(f"Detected {NUM_CLASSES} classes: {trainDataset.classes}")

model = models.efficientnet_b0(pretrained=False)
model.classifier[1] = torch.nn.Linear(model.classifier[1].in_features, NUM_CLASSES)

model.load_state_dict(torch.load(MODEL_WEIGHTS, map_location=DEVICE))
model.eval()

exampleInput = torch.rand(1, 3, 224, 224)

tracedScriptModule = torch.jit.trace(model, exampleInput)
tracedScriptModule.save(ANDROID_MODEL_PATH)

print(f"TorchScript model saved as {ANDROID_MODEL_PATH}")
