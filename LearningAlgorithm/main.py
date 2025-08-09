import os
import torch
import torch.nn as nn
import torch.optim as optim
import matplotlib.pyplot as plt

from TrainingUtility import model_training
from ModelConverterUtility import initialize_model, convert_model_to_torchscript
from DataUtility import get_data_loaders

BASE_DIR = os.path.dirname(__file__)
DATABASE_DIR = os.path.join(BASE_DIR, '../Database')
TRAINING_FEEDBACK_DIR = os.path.join(BASE_DIR, '../TrainingFeedback')
MODELS_DIR = os.path.join(BASE_DIR, '../Models')
MODEL_WEIGHTS_DIR = os.path.join(MODELS_DIR, 'ModelWeights')
MODEL_WEIGHTS_PATH = os.path.join(MODEL_WEIGHTS_DIR, 'plant_classifier_efficientnetb0.pth')
ANDROID_MODEL_PATH = os.path.join(BASE_DIR, '../Application/app/src/main/assets/plant_classifier_efficientnetb0.pt')

os.makedirs(TRAINING_FEEDBACK_DIR, exist_ok=True)
os.makedirs(MODEL_WEIGHTS_DIR, exist_ok=True)
os.makedirs(os.path.dirname(ANDROID_MODEL_PATH), exist_ok=True)

BATCH_SIZE = 64
NUM_OF_EPOCHS = 1
NUM_OF_WORKERS = 8
LEARNING_RATE = 1e-4

DEVICE = torch.device("cuda" if torch.cuda.is_available() else "cpu")

def main():
    train_loader, val_loader, class_names = get_data_loaders(DATABASE_DIR, batch_size=BATCH_SIZE, num_workers=NUM_OF_WORKERS)
    num_classes = len(class_names)
    print(f"Detected {num_classes} classes: {class_names}")

    model = initialize_model(num_classes, DEVICE)
    criterion = nn.CrossEntropyLoss()
    optimizer = optim.Adam(model.parameters(), lr=LEARNING_RATE)

    dataloaders = {'train': train_loader, 'val': val_loader}

    model, train_loss, val_loss, val_accuracy_history = model_training(model, dataloaders, criterion, optimizer, DEVICE, num_of_epochs=NUM_OF_EPOCHS, patience=5)

    torch.save(model.state_dict(), MODEL_WEIGHTS_PATH)
    print("Model weights saved!")

    plt.figure(figsize=(10, 6))
    plt.plot(train_loss, label='Train Loss')
    plt.plot(val_loss, label='Val Loss')
    plt.plot(val_accuracy_history, label='Val Accuracy')
    plt.legend()
    plt.title('Loss and Validation Accuracy over Epochs')
    plt.xlabel('Epoch')
    plt.ylabel('Value')
    plt.savefig(os.path.join(TRAINING_FEEDBACK_DIR, 'training_plot.png'))

    with open(os.path.join(TRAINING_FEEDBACK_DIR, 'val_accuracy_per_epoch.txt'), 'w') as f:
        for i, acc in enumerate(val_accuracy_history):
            f.write(f"Epoch {i+1}: {acc:.4f}\n")

    convert_model_to_torchscript(model, ANDROID_MODEL_PATH, DEVICE)

if __name__ == "__main__":
    main()
