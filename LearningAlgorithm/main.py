import os
import torch
import torch.nn as nn
import torch.optim as optim
import matplotlib.pyplot as plt

from TrainingUtility import modelTraining
from ModelUtility import initializeModel, convertModelToAndroidModel, copyPlantClassesToApplication
from DataUtility import getDataLoaders

BASE_DIR = os.path.dirname(__file__)
DATABASE_DIR = os.path.join(BASE_DIR, '../Database')
TRAINING_FEEDBACK_DIR = os.path.join(BASE_DIR, '../TrainingFeedback')
MODELS_DIR = os.path.join(BASE_DIR, '../Models')
MODEL_WEIGHTS_DIR = os.path.join(MODELS_DIR, 'ModelWeights')
MODEL_WEIGHTS_PATH = os.path.join(MODEL_WEIGHTS_DIR, 'plant_classifier_efficientnetb0.pth')
ANDROID_MODEL_DIR = os.path.join(MODELS_DIR, 'AndroidModel')
ANDROID_MODEL_PATH = os.path.join(ANDROID_MODEL_DIR, 'plant_classifier_efficientnetb0.ptl')
ANDROID_MODEL_APP_PATH = os.path.join(BASE_DIR, '../Application/app/src/main/assets/plant_classifier_efficientnetb0.ptl')
PLANT_CALLSSES_APP_PATH = os.path.join(BASE_DIR, '../Application/app/src/main/assets/labels.txt')

os.makedirs(TRAINING_FEEDBACK_DIR, exist_ok=True)
os.makedirs(MODEL_WEIGHTS_DIR, exist_ok=True)
os.makedirs(ANDROID_MODEL_DIR, exist_ok=True)
os.makedirs(os.path.dirname(ANDROID_MODEL_APP_PATH), exist_ok=True)
os.makedirs(os.path.dirname(PLANT_CALLSSES_APP_PATH), exist_ok=True)

BATCH_SIZE = 16
NUM_OF_EPOCHS = 1
NUM_OF_WORKERS = 8
LEARNING_RATE = 1e-4
PATIENCE = 5

DEVICE = torch.device("cuda" if torch.cuda.is_available() else "cpu")

def main():
    trainingLoader, validationLoader, classNames = getDataLoaders(DATABASE_DIR, batchSize=BATCH_SIZE, numOfWorkers=NUM_OF_WORKERS)
    numClasses = len(classNames)
    print(f"Detected {numClasses} classes: {classNames}")

    model = initializeModel(numClasses, DEVICE)
    criterion = nn.CrossEntropyLoss()
    optimizer = optim.AdamW(model.parameters(), lr=LEARNING_RATE)

    dataloaders = {'train': trainingLoader, 'val': validationLoader}

    model, trainingLoss, validationLoss, validationAccuracyHistory = modelTraining(model, dataloaders, criterion, optimizer, DEVICE, numOfEpochs=NUM_OF_EPOCHS, patience=PATIENCE)

    torch.save(model.state_dict(), MODEL_WEIGHTS_PATH)
    print("Model weights saved!")

    plt.figure(figsize=(10, 6))
    plt.plot(trainingLoss, label='Training Loss')
    plt.plot(validationLoss, label='Validation Loss')
    plt.plot(validationAccuracyHistory, label='Val Accuracy')
    plt.legend()
    plt.title('Loss and Validation Accuracy over Epochs')
    plt.xlabel('Epoch')
    plt.ylabel('Value')
    plt.savefig(os.path.join(TRAINING_FEEDBACK_DIR, 'TrainingPlot.png'))

    with open(os.path.join(TRAINING_FEEDBACK_DIR, 'ValAccuracyperEpoch.txt'), 'w') as f:
        for i, acc in enumerate(validationAccuracyHistory):
            f.write(f"Epoch {i+1}: {acc:.4f}\n")

    convertModelToAndroidModel(model, MODEL_WEIGHTS_PATH, ANDROID_MODEL_PATH, ANDROID_MODEL_APP_PATH)
    copyPlantClassesToApplication(DATABASE_DIR, PLANT_CALLSSES_APP_PATH)

if __name__ == "__main__":
    main()
