import copy
import torch

def model_training(model, dataloaders, criterion, optimizer, device, num_of_epochs=30, patience=5):
    best_model_weights = copy.deepcopy(model.state_dict())
    best_validation_accuracy = 0.0
    train_loss_history = []
    val_loss_history = []
    val_accuracy_history = []

    best_val_loss = float('inf')
    epochs_with_no_improvement = 0

    for epoch in range(num_of_epochs):
        print(f'\nEpoch {epoch + 1}/{num_of_epochs}')
        print('-' * 20)

        for phase in ['train', 'val']:
            if phase == 'train':
                model.train()
            else:
                model.eval()

            running_loss, running_corrects = 0.0, 0

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

                running_loss += loss.item() * inputs.size(0)
                running_corrects += torch.sum(preds == labels.data)

            epoch_loss = running_loss / len(dataloaders[phase].dataset)
            epoch_accuracy = running_corrects.double() / len(dataloaders[phase].dataset)

            print(f'{phase} Loss: {epoch_loss:.4f} Accuracy: {epoch_accuracy:.4f}')

            if phase == 'train':
                train_loss_history.append(epoch_loss)
            else:
                val_loss_history.append(epoch_loss)
                val_accuracy_history.append(epoch_accuracy.item())
                if epoch_loss < best_val_loss:
                    best_val_loss = epoch_loss
                    epochs_with_no_improvement = 0
                else:
                    epochs_with_no_improvement += 1
                if epoch_accuracy > best_validation_accuracy:
                    best_validation_accuracy = epoch_accuracy
                    best_model_weights = copy.deepcopy(model.state_dict())

        if epochs_with_no_improvement >= patience:
            print(f"Early stopping triggered at epoch {epoch + 1}")
            break

    print(f'\nBest val Acc: {best_validation_accuracy:.4f}')
    model.load_state_dict(best_model_weights)
    return model, train_loss_history, val_loss_history, val_accuracy_history
