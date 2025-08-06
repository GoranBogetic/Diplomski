import torch
import torch.nn as nn
import torch.optim as optim
from torchvision import datasets, transforms, models
from torch.utils.data import DataLoader
import os
import copy
import matplotlib.pyplot as plt

# PyTorch configuration
DATABASE_DIR = os.path.join(os.path.dirname(__file__), '../Database')
TRAINING_FEEDBACK_DIR = os.path.join(os.path.dirname(__file__), '../TrainingFeedback')
MODELS_DIR = os.path.join(os.path.dirname(__file__), '../Models')
BATCH_SIZE = 64
NUM_OF_EPOCHS = 30
NUM_OF_WORKERS = 8
LEARNING_RATE = 1e-4
# Ran on Laptop RTX 4060
DEVICE = torch.device("cuda" if torch.cuda.is_available() else "cpu")

# Training and Validation Transforms
transformTrain = transforms.Compose([
    transforms.RandomResizedCrop(224, scale=(0.8, 1.0)),
    transforms.RandomHorizontalFlip(),
    transforms.RandomVerticalFlip(),
    transforms.RandomRotation(degrees = 30),
    transforms.ColorJitter(brightness = 0.4, contrast = 0.4, saturation = 0.3, hue = 0.1),
    transforms.RandomAffine(degrees = 0, translate = (0.15, 0.15), scale = (0.9, 1.1)),
    transforms.RandomPerspective(distortion_scale = 0.2, p = 0.5), 
    transforms.ToTensor(),
    transforms.Normalize([0.485, 0.456, 0.406],
                         [0.229, 0.224, 0.225]),
    transforms.RandomErasing(p = 0.3, scale = (0.02, 0.15))
])

transformVal = transforms.Compose([
    transforms.Resize(256),
    transforms.CenterCrop(224),
    transforms.ToTensor(),
    transforms.Normalize([0.485, 0.456, 0.406],
                         [0.229, 0.224, 0.225])
])

# Train and Validation Datasets
trainDataset = datasets.ImageFolder(os.path.join(DATABASE_DIR, 'train'), transform = transformTrain)
valDataset = datasets.ImageFolder(os.path.join(DATABASE_DIR, 'val'), transform = transformVal)

# Train and Validation DataLoaders
trainLoader = DataLoader(trainDataset, batch_size = BATCH_SIZE, shuffle = True, num_workers = NUM_OF_WORKERS)
valLoader = DataLoader(valDataset, batch_size = BATCH_SIZE, shuffle = False, num_workers = NUM_OF_WORKERS)

classNames = trainDataset.classes
numOfClasses = len(classNames)

# Model Initialization
model = models.efficientnet_b0(pretrained=True)
model.classifier[1] = nn.Linear(model.classifier[1].in_features, numOfClasses)
model = model.to(DEVICE)

# Loss function and Optimizer
lossFunction = nn.CrossEntropyLoss()
optimizer = optim.Adam(model.parameters(), lr=LEARNING_RATE)

# Model Training Function
def modelTraining(model, dataloaders, criterion, optimizer, numOfEpochs=NUM_OF_EPOCHS, patience=5):
    bestModelWeights = copy.deepcopy(model.state_dict())
    bestValidationAccuracy = 0.0
    trainLossHistory = []
    valLossHistory = []
    valAccuracyHistory = []

    bestValLoss = float('inf')
    epochsWithNoImprovement = 0

    for epoch in range(numOfEpochs):
        print(f'\nEpoch {epoch + 1}/{numOfEpochs}')
        print('-' * 20)

        for phase in ['train', 'val']:
            if phase == 'train':
                model.train()
            else:
                model.eval()
            runningLoss, runningCorrects = 0.0, 0

            for inputs, labels in dataloaders[phase]:
                inputs, labels = inputs.to(DEVICE), labels.to(DEVICE)
                optimizer.zero_grad()

                with torch.set_grad_enabled(phase == 'train'):
                    outputs = model(inputs)
                    _, preds = torch.max(outputs, 1)
                    loss = criterion(outputs, labels)

                    if phase == 'train':
                        loss.backward()
                        optimizer.step()

                runningLoss += loss.item() * inputs.size(0)
                runningCorrects += torch.sum(preds == labels.data)

            epochLoss = runningLoss / len(dataloaders[phase].dataset)
            epochAccuracy = runningCorrects.double() / len(dataloaders[phase].dataset)

            print(f'{phase} Loss: {epochLoss:.4f} Accuracy: {epochAccuracy:.4f}')

            if phase == 'train':
                trainLossHistory.append(epochLoss)
            else:
                valLossHistory.append(epochLoss)
                valAccuracyHistory.append(epochAccuracy.item())
                # Early stopping logic
                if epochLoss < bestValLoss:
                    bestValLoss = epochLoss
                    epochsWithNoImprovement = 0
                else:
                    epochsWithNoImprovement += 1
                if epochAccuracy > bestValidationAccuracy:
                    bestValidationAccuracy = epochAccuracy
                    bestModelWeights = copy.deepcopy(model.state_dict())

        # Check early stopping condition after each epoch
        if epochsWithNoImprovement >= patience:
            print(f"Early stopping triggered at epoch {epoch + 1}")
            break

    print(f'\nBest val Acc: {bestValidationAccuracy:.4f}')
    model.load_state_dict(bestModelWeights)
    return model, trainLossHistory, valLossHistory, valAccuracyHistory

if __name__ == "__main__":
    # Training the model
    dataloaders = {'train': trainLoader, 'val': valLoader}
    model, trainLoss, valLoss, valAccuracyHistory = modelTraining(model, dataloaders, lossFunction, optimizer, numOfEpochs=NUM_OF_EPOCHS, patience=5)

    # Ensure directories exist
    os.makedirs(MODELS_DIR, exist_ok=True)

    # Saving the model weights (.pth)
    torch.save(model.state_dict(), (os.path.join(MODELS_DIR, 'ModelWeights/plant_classifier_efficientnetb0.pth')))
    print("Model saved!")

    os.makedirs(TRAINING_FEEDBACK_DIR, exist_ok=True)
    # Plotting the training and validation loss
    plt.figure(figsize=(10, 6))
    plt.plot(trainLoss, label='Train Loss')
    plt.plot(valLoss, label='Val Loss')
    plt.plot(valAccuracyHistory, label='Val Accuracy')
    plt.legend()
    plt.title('Loss and Validation Accuracy over Epochs')
    plt.xlabel('Epoch')
    plt.ylabel('Value')
    plt.savefig(os.path.join(TRAINING_FEEDBACK_DIR, 'training_plot.png'))

    # Here is how you would save it:
    if 'valAccuracyHistory' in locals():
        with open(os.path.join(TRAINING_FEEDBACK_DIR, 'val_accuracy_per_epoch.txt'), 'w') as f:
            for i, acc in enumerate(valAccuracyHistory):
                f.write(f"Epoch {i+1}: {acc:.4f}\n")