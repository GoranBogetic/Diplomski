from torchvision import datasets, transforms
from torch.utils.data import DataLoader
import os

def getDataLoaders(databaseDir, batchSize, numOfWorkers):
    transformTrainingData = transforms.Compose([
        transforms.RandomResizedCrop(224, scale=(0.8, 1.0)),
        transforms.RandomHorizontalFlip(),
        transforms.RandomVerticalFlip(),
        transforms.RandomRotation(degrees=30),
        transforms.ColorJitter(brightness=0.4, contrast=0.4, saturation=0.3, hue=0.1),
        transforms.RandomAffine(degrees=0, translate=(0.15, 0.15), scale=(0.9, 1.1)),
        transforms.RandomPerspective(distortion_scale=0.2, p=0.5),
        transforms.ToTensor(),
        transforms.Normalize([0.485, 0.456, 0.406],
                             [0.229, 0.224, 0.225]),
        transforms.RandomErasing(p=0.3, scale=(0.02, 0.15))
    ])

    transformValidationData = transforms.Compose([
        transforms.Resize(256),
        transforms.CenterCrop(224),
        transforms.ToTensor(),
        transforms.Normalize([0.485, 0.456, 0.406],
                             [0.229, 0.224, 0.225])
    ])

    trainingDataset = datasets.ImageFolder(os.path.join(databaseDir, 'train'), transform=transformTrainingData)
    validationDataset = datasets.ImageFolder(os.path.join(databaseDir, 'val'), transform=transformValidationData)

    trainingLoader = DataLoader(trainingDataset, batch_size=batchSize, shuffle=True, num_workers=numOfWorkers)
    validationLoader = DataLoader(validationDataset, batch_size=batchSize, shuffle=False, num_workers=numOfWorkers)

    return trainingLoader, validationLoader, trainingDataset.classes
