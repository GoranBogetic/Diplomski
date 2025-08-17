import os
import requests
from concurrent.futures import ThreadPoolExecutor, as_completed
from SpeciesList import speciesList

# ==== CONFIGURATION ====
BASE_DIR = os.path.dirname(__file__)
DATABASE_DIR = os.path.join(BASE_DIR, '../Database')
SOURCES_DIR = os.path.join(BASE_DIR, '../Sources')

TRAIN_DIR = os.path.join(DATABASE_DIR, 'train')
VAL_DIR = os.path.join(DATABASE_DIR, 'val')

THREADS_PER_SPECIES = 200
URLS_LOG_PATH = os.path.join(SOURCES_DIR, "ImageSources.txt")


# ==== DOWNLOAD HELPERS ====
def downloadImage(url, save_path):
    """Download a single image from a URL."""
    try:
        response = requests.get(url, timeout=15, stream=True)
        response.raise_for_status()
        with open(save_path, "wb") as f:
            for chunk in response.iter_content(8192):
                if chunk:
                    f.write(chunk)
        return True
    except Exception as e:
        print(f"Failed to download {url}: {e}")
        return False


def downloadImagesForFolder(speciesName, urls, outDir, threads):
    """Download all URLs into a folder in parallel."""
    os.makedirs(outDir, exist_ok=True)
    count = 0

    with ThreadPoolExecutor(max_workers=threads) as executor:
        futureToUrl = {}
        for idx, url in enumerate(urls):
            fileName = f"{speciesName}_{idx}.jpg"
            filePath = os.path.join(outDir, fileName)
            if os.path.exists(filePath):
                continue
            future = executor.submit(downloadImage, url, filePath)
            futureToUrl[future] = url

        for future in as_completed(futureToUrl):
            if future.result():
                count += 1

    print(f"Downloaded {count} images for {speciesName} in {outDir}")
    return count


# ==== PARSING HELPER ====
def parseImageSources(filePath):
    """
    Parse ImageSources.txt into a dict:
    {speciesName: {"train": [urls], "val": [urls]}}
    """
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
                    # default to train if no prefix
                    speciesDict[currentSpecies]["train"].append(line)
    return speciesDict

def writeClassnames(trainDir, valDir, speciesList):
    """Create classnames.txt in train and val folders containing species names."""
    classnames = [name for name, _ in speciesList]
    for folder in [trainDir, valDir]:
        with open(os.path.join(folder, "classnames.txt"), "w") as f:
            f.write("\n".join(classnames))
    print("classnames.txt created in train and val folders.")


# ==== MAIN RECONSTRUCTION ====
def reconstructDatabaseWithSplit():
    os.makedirs(TRAIN_DIR, exist_ok=True)
    os.makedirs(VAL_DIR, exist_ok=True)

    speciesUrls = parseImageSources(URLS_LOG_PATH)

    for speciesName, data in speciesUrls.items():
        trainUrls = data.get("train", [])
        valUrls = data.get("val", [])

        print(f"Reconstructing {speciesName}: {len(trainUrls)} train, {len(valUrls)} val")

        downloadImagesForFolder(speciesName, trainUrls, os.path.join(TRAIN_DIR, speciesName), THREADS_PER_SPECIES)
        downloadImagesForFolder(speciesName, valUrls, os.path.join(VAL_DIR, speciesName), THREADS_PER_SPECIES)

    print("Database reconstruction with train/val split complete.")


if __name__ == "__main__":
    reconstructDatabaseWithSplit()
    writeClassnames(TRAIN_DIR, VAL_DIR, speciesList)
