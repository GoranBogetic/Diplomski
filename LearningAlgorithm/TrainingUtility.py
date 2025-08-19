import copy
import torch

def modelTraining(model, dataloaders, criterion, optimizer, device, numOfEpochs, patience):
    bestModelWeights = copy.deepcopy(model.state_dict())
    bestValidationAccuracy = 0.0
    trainingLossHistory = []
    validationLossHistory = []
    validationAccuracyHistory = []

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
                inputs, labels = inputs.to(device), labels.to(device)
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
                trainingLossHistory.append(epochLoss)
            else:
                validationLossHistory.append(epochLoss)
                validationAccuracyHistory.append(epochAccuracy.item())

                if epochAccuracy > bestValidationAccuracy:
                    bestValidationAccuracy = epochAccuracy
                    bestModelWeights = copy.deepcopy(model.state_dict())
                    epochsWithNoImprovement = 0
                else:
                    epochsWithNoImprovement += 1

        if epochsWithNoImprovement >= patience:
            print(f"Early stopping triggered at epoch {epoch + 1}")
            break

    print(f'\nBest val Acc: {bestValidationAccuracy:.4f}')
    model.load_state_dict(bestModelWeights)
    return model, trainingLossHistory, validationLossHistory, validationAccuracyHistory
