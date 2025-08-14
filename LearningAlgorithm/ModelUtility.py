import os
import shutil
import torch
from torchvision import models

def initializeModel(numClasses, device, pretrained=True):
    model = models.efficientnet_b0(pretrained=pretrained)
    model.classifier[1] = torch.nn.Linear(model.classifier[1].in_features, numClasses)
    model = model.to(device)
    return model

def convertModelToAndroidModel(model, androidModelPath, androidModelAppPath, device):
    model.eval()
    exampleInput = torch.rand(1, 3, 224, 224).to(device)
    androidModel = torch.jit.trace(model, exampleInput)
    
    androidModel.save(androidModelPath)
    print(f"Android model model saved as {androidModelPath}")
    
    androidModel.save(androidModelAppPath)
    print(f"Android model model also saved as {androidModelAppPath}")


def copyPlantClassesToApplication(databaseDir, plantClassesFilePath):
    train_dir = os.path.join(databaseDir, 'train')
    
    txt_files = [f for f in os.listdir(train_dir) if f.endswith('.txt')]
    if not txt_files:
        print("No .txt files found in train directory.")
        return
    
    source_file = os.path.join(train_dir, txt_files[0])
    shutil.copyfile(source_file, plantClassesFilePath)
    print(f"Labels file copied from {source_file} to {plantClassesFilePath}")
