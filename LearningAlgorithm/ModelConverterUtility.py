import torch
from torchvision import models

def initializeModel(num_classes, device, pretrained=True):
    model = models.efficientnet_b0(pretrained=pretrained)
    model.classifier[1] = torch.nn.Linear(model.classifier[1].in_features, num_classes)
    model = model.to(device)
    return model

def convertModelToTorchscript(model, android_model_path, device):
    model.eval()
    example_input = torch.rand(1, 3, 224, 224).to(device)
    traced_script_module = torch.jit.trace(model, example_input)
    traced_script_module.save(android_model_path)
    print(f"TorchScript model saved as {android_model_path}")
