import os
import shutil
import torch
from torchvision import models
from torchvision.models.efficientnet import EfficientNet_B3_Weights

# Training is done on Nvidia RTX 4060 laptop GPU
# Batch size and Number of workers are set like this to prevent out of memory errors
BATCH_SIZE = 8 # could be 16 for efficientnet-b3
NUM_OF_EPOCHS = 30
NUM_OF_WORKERS = 4
LEARNING_RATE = 1e-4
# Determines how many epochs to wait for improvement before stopping training
PATIENCE = 4

DEVICE = torch.device("cuda" if torch.cuda.is_available() else "cpu")

def initializeModel(numClasses, device):
    model = models.efficientnet_b3(weights=EfficientNet_B3_Weights.DEFAULT)
    model.classifier[1] = torch.nn.Linear(model.classifier[1].in_features, numClasses)
    model = model.to(device)
    return model

def convertModelToAndroidModel(model, modelWeightsPath, androidModelPath, androidModelAppPath):
    # Ensure output directory exists
    os.makedirs(os.path.dirname(androidModelPath), exist_ok=True)
    if androidModelAppPath:
        os.makedirs(os.path.dirname(androidModelAppPath), exist_ok=True)

    # Determine device for loading weights
    device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')
    print(f"Using device: {device} to load weights.")

    # Load weights
    try:
        state_dict = torch.load(modelWeightsPath, map_location=device)
        # Remove 'module.' prefix if present
        if any(k.startswith('module.') for k in state_dict.keys()):
            state_dict = {k.replace('module.', ''): v for k, v in state_dict.items()}
        model.load_state_dict(state_dict)
        print(f"Loaded weights from {modelWeightsPath}")
    except Exception as e:
        print(f"Error loading weights: {e}")
        return

    # Trace model for TorchScript
    try:
        model.eval()
        modelCpu = model.to('cpu')
        exampleInput = torch.rand(1, 3, 224, 224)
        tracedModel = torch.jit.trace(modelCpu, exampleInput)

        tracedModel.save(androidModelPath)
        print(f"Android model saved to {androidModelPath}")

        if androidModelAppPath:
            tracedModel.save(androidModelAppPath)
            print(f"Android model also saved to {androidModelAppPath}")
    except Exception as e:
        print(f"Error tracing/saving model: {e}")


def copyPlantClassesToApplication(databaseDir, plantClassesFilePath):
    trainDir = os.path.join(databaseDir, 'train')
    
    txtFiles = [f for f in os.listdir(trainDir) if f.endswith('.txt')]
    if not txtFiles:
        print("No .txt files found in train directory.")
        return
    
    sourceFile = os.path.join(trainDir, txtFiles[0])
    shutil.copyfile(sourceFile, plantClassesFilePath)
    print(f"Labels file copied from {sourceFile} to {plantClassesFilePath}")
