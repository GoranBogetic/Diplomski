APK with application named app-debug.apk can be downloaded from [here](https://fesb-my.sharepoint.com/:u:/g/personal/gboget00_fesb_hr/EeDeSSBf0A1Lr-Ajz5gDpbABd-GnlJI7gDVrPk_wrzepfA?e=iXcbVp). \
Written work can be found [here](https://github.com/GoranBogetic/Diplomski/tree/main/Word).\
Image attributions for images whit CC-BY license can be found [here](https://github.com/GoranBogetic/Diplomski/blob/main/Sources/ImageAttributions.txt)

----

# Quick start guide
Requirements: Python 3.9+, PyTorch & TorchVision, Matplotlib, Requests

If not installed already run following command:

- pip install torch torchvision matplotlib requests

\
\
(Optional) Reconstruct the dataset from logs run:

- python .\Utility\DatabaseReconstructor.py

\
\
Train and export the model for Android run:

- python .\LearningAlgorithm\main.py


---
\
\
Artifacts you should see afterwards:

- Models/ModelWeights/plant_classifier_efficientnetb3.pth
- Models/AndroidModel/plant_classifier_efficientnetb3.ptl
- TrainingFeedback/TrainingPlot.png, TrainingFeedback/ValAccuracyperEpoch.txt
- Application/app/src/main/assets/plant_classifier_efficientnetb3.ptl
- Application/app/src/main/assets/labels.txt

\
\
Quick start — Android app

1. Open the project in Android Studio from Application/ (minSdk 24, targetSdk 36).

2. Ensure assets exist: app/src/main/assets/plant_classifier_efficientnetb3.ptl and app/src/main/assets/labels.txt.

3. Build & run on a device. The app loads the TorchScript model, lets you capture or pick an image (CameraX / Gallery), preprocesses (300×300, ImageNet normalization), performs on-device inference, and shows the predicted species in a BottomSheet with local info.

---

\
\
File-by-file short explanation

- LearningAlgorithm (Python)
    - main.py — Orchestrates training: builds dataloaders,     initializes EfficientNet-B3 head for N classes, trains with early stopping, saves .pth, exports .ptl, copies labels.txt to Android assets, and writes plots/metrics.

    - TrainingUtility.py — Core training loop with early stopping; tracks train/val loss and validation accuracy; restores best weights.

    - ModelUtility.py — Initializes EfficientNet-B3 (pretrained), swaps classifier head; loads weights; traces to TorchScript with a 224×224 example; saves .ptl; copies labels.txt to the app.

    - Training/DataUtility.py — TorchVision datasets and dataloaders. Strong training augmentations; deterministic validation transforms (resize/center-crop/normalize).

- Utility (Python)
    - SpeciesList.py — List of (speciesName, taxonId) used by the dataset scripts.

    - DatabaseCreator.py — Automated collection via iNaturalist API; CC0/CC-BY filter; concurrent downloads; CC-BY attributions; per-species train/val split; URL logging; classnames.txt.

    - DatabaseReconstructor.py — Rebuilds Database/train and Database/val from Sources/ImageSources.txt; regenerates classnames.txt.

- Database and logs
    - Database/train/, Database/val/ — Image folders per class for training and validation, each including classnames.txt.

    - Sources/ImageSources.txt — Plain-text log of image URLs per species with train:/val: prefixes for reproducibility.

    - Sources/ImageAttributions.txt — CC-BY attribution lines (license, attribution text, URL), grouped per species.

- Models and feedback
    - Models/ModelWeights/plant_classifier_efficientnetb3.pth — Saved PyTorch weights (state_dict).

    - Models/AndroidModel/plant_classifier_efficientnetb3.ptl — TorchScript model for Android.

    - TrainingFeedback/TrainingPlot.png (generated after LearningAlforithm) — Loss and validation accuracy over epochs.

    - TrainingFeedback/ValAccuracyperEpoch.txt (generated after LearningAlforithm) — Per-epoch validation accuracy log.

- Android application
    - Application/app/src/main/AndroidManifest.xml — CAMERA and media-read permissions; launcher activity.

    - Application/app/src/main/assets/plant_classifier_efficientnetb3.ptl — TorchScript model packaged with the app.

    - Application/app/src/main/assets/labels.txt — Class names.

    - Application/app/src/main/java/com/example/plantfinder/MainActivity.kt — CameraX preview/capture and gallery pick; EXIF rotation; preprocessing to 300×300 with ImageNet normalization; TorchScript inference; argmax→label mapping; BottomSheet result; scanning animation; permissions and lifecycle handling.

    - Application/app/src/main/java/com/example/plantfinder/PlantDatabase.kt — Local metadata map (plantInfo) with English/Croatian names, Latin names, descriptions, and edibility flags.

    - Application/app/src/main/res/layout/activity_main.xml — Main screen: PreviewView, ImageView, scanning overlay, gallery button, shutter button.

    - Application/app/src/main/res/layout/bottom_sheet_layout.xml — BottomSheet layout for results (names, Latin, description, edibility).

    - Application/app/src/main/res/drawable/scanning_bar.xml — Gradient overlay used as the animated “scanning” bar.

    - Application/app/src/main/res/drawable/shutter_button_background.xml — Layered oval background for the shutter button.

    - Application/app/src/main/res/animator/shutter_button_scale.xml — State-list animator for the shutter press effect.

    - Application/app/build.gradle.kts — Module build config and dependencies (PyTorch Mobile, CameraX, Compose/Material3).