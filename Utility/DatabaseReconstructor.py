import os
import requests
from concurrent.futures import ThreadPoolExecutor, as_completed
from SpeciesList import speciesList

BASE_DIR = os.path.dirname(__file__)
DATABASE_DIR = os.path.join(BASE_DIR, '../Database')
SOURCES_DIR = os.path.join(BASE_DIR, '../Sources')

TRAIN_DIR = os.path.join(DATABASE_DIR, 'train')
VAL_DIR = os.path.join(DATABASE_DIR, 'val')

THREADS_PER_SPECIES = 160
URLS_LOG_PATH = os.path.join(SOURCES_DIR, "ImageSources.txt")

GLOBAL_EXECUTOR = ThreadPoolExecutor(max_workers=THREADS_PER_SPECIES)

def downloadImage(url, savePath):
    try:
        response = requests.get(url, timeout=15, stream=True)
        response.raise_for_status()
        with open(savePath, "wb") as f:
            for chunk in response.iter_content(8192):
                if chunk:
                    f.write(chunk)
        return True
    except Exception as e:
        print(f"Failed to download {url}: {e}")
        return False

def downloadImagesForFolder(speciesName, urls, outDir):
    os.makedirs(outDir, exist_ok=True)
    downloadTasks = []
    count = 0

    for idx, url in enumerate(urls):
        fileName = f"{speciesName}_{idx}.jpg"
        filePath = os.path.join(outDir, fileName)

        if os.path.exists(filePath):
            continue

        task = GLOBAL_EXECUTOR.submit(downloadImage, url, filePath)
        downloadTasks.append((task, url))

    taskToUrl = {task: url for task, url in downloadTasks}
    for future in as_completed(taskToUrl):
        url = taskToUrl[future]
        if future.result():
            count += 1


    print(f"Downloaded {count} images for {speciesName} in {outDir}")
    return count

#  Parse ImageSources.txt into a dict:
#  {speciesName: {"train": [urls], "val": [urls]}}
def parseImageSources(filePath):
    speciesDict = {}
    currentSpecies = None

    if not os.path.exists(filePath):
        print(f"{filePath} does not exist!")
        return speciesDict

    with open(filePath, "r") as f:
        for line in f:
            line = line.strip()
            if line.startswith("===") and line.endswith("==="):
                currentSpecies = line.strip("= ").strip()
                speciesDict[currentSpecies] = {"train": [], "val": []}
            elif line and currentSpecies:
                if line.startswith("train:"):
                    speciesDict[currentSpecies]["train"].append(line[len("train:"):].strip())
                elif line.startswith("val:"):
                    speciesDict[currentSpecies]["val"].append(line[len("val:"):].strip())
                else:
                    speciesDict[currentSpecies]["train"].append(line)
    return speciesDict

def writeClassnames(trainDir, valDir, speciesList):
    classnames = [name for name, _ in speciesList]
    for folder in [trainDir, valDir]:
        with open(os.path.join(folder, "classnames.txt"), "w") as f:
            f.write("\n".join(classnames))
    print("classnames.txt created in train and val folders.")

def reconstructDatabaseWithSplit():
    os.makedirs(TRAIN_DIR, exist_ok=True)
    os.makedirs(VAL_DIR, exist_ok=True)

    speciesUrls = parseImageSources(URLS_LOG_PATH)

    for speciesName, data in speciesUrls.items():
        trainUrls = data.get("train", [])
        valUrls = data.get("val", [])

        print(f"Reconstructing {speciesName}: {len(trainUrls)} train, {len(valUrls)} val")

        downloadImagesForFolder(speciesName, trainUrls, os.path.join(TRAIN_DIR, speciesName))
        downloadImagesForFolder(speciesName, valUrls, os.path.join(VAL_DIR, speciesName))

    print("Database reconstruction with train/val split complete.")

# ==== ENTRY POINT ====
if __name__ == "__main__":
    reconstructDatabaseWithSplit()
    writeClassnames(TRAIN_DIR, VAL_DIR, speciesList)
    GLOBAL_EXECUTOR.shutdown(wait=True)
