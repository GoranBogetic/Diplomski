import os
import requests
import random
import shutil
import time
from concurrent.futures import ThreadPoolExecutor
from SpeciesList import speciesList

BASE_DIR = os.path.dirname(__file__)
DATABASE_DIR = os.path.join(BASE_DIR, '../Database')
SOURCES_DIR = os.path.join(BASE_DIR, '../Sources')

TRAIN_DIR = os.path.join(DATABASE_DIR, 'train')
VAL_DIR = os.path.join(DATABASE_DIR, 'val')

LICENSES = ["cc0", "cc-by"]   # only fetch images with these licenses from API
PER_PAGE = 200
MAX_IMAGES_PER_SPECIES = 800
MAX_PAGES = 20
VAL_SPLIT = 0.125
GLOBAL_THREADS = 160  # global thread pool size

URLS_LOG_PATH = os.path.join(SOURCES_DIR, "ImageSources.txt")
ATTRIBUTIONS_PATH = os.path.join(SOURCES_DIR, "ImageAttributions.txt")

GLOBAL_EXECUTOR = ThreadPoolExecutor(max_workers=GLOBAL_THREADS)

# Fetch one page of observations from iNaturalist API
def fetchFromPage(taxonId, licenses, page, perPage):
    url = "https://api.inaturalist.org/v1/observations"
    params = {
        "taxon_id": taxonId,
        "photo_license": ",".join(licenses),
        "per_page": perPage,
        "page": page
    }
    response = requests.get(url, params=params, timeout=15)
    response.raise_for_status()
    return response.json()

def downloadImage(url, savePath):
    try:
        imgData = requests.get(url, timeout=15).content
        with open(savePath, 'wb') as handler:
            handler.write(imgData)
        return True
    except Exception as e:
        print(f"Failed to save {url}: {e}")
        return False

# Download all images for one species and log attributions
def processSpecies(species, attributionFile):
    speciesName, taxonId = species
    speciesFolder = os.path.join(TRAIN_DIR, speciesName)
    os.makedirs(speciesFolder, exist_ok=True)

    count = 0
    allDownloadedFiles = []

    attributionFile.write(f"=== {speciesName} ===\n")

    for page in range(1, MAX_PAGES + 1):
        if count >= MAX_IMAGES_PER_SPECIES:
            print(f"Reached max images for {speciesName}")
            break

        try:
            data = fetchFromPage(taxonId, LICENSES, page, PER_PAGE)
        except Exception as e:
            print(f"Error fetching page {page} for {speciesName}: {e}")
            break

        results = data.get('results', [])
        if not results:
            break

        downloadTasks = []
        for obs in results:
            for photo in obs.get('photos', []):
                if count + len(downloadTasks) >= MAX_IMAGES_PER_SPECIES:
                    break

                license_code = photo.get("license_code")
                attribution = photo.get("attribution")
                photoUrl = photo['url'].replace('square', 'original')

                if not license_code:
                    continue

                # Record attribution only for cc-by
                if license_code in ["cc-by"] and attribution:
                    attributionFile.write(f"{license_code}: {attribution} ({photoUrl})\n")

                # Download if cc0 or cc-by
                if license_code in ["cc0", "cc-by"]:
                    fileName = f"{speciesName}_{count + len(downloadTasks)}.jpg"
                    filePath = os.path.join(speciesFolder, fileName)
                    task = GLOBAL_EXECUTOR.submit(downloadImage, photoUrl, filePath)
                    downloadTasks.append((task, photoUrl, filePath))

        # Collect results
        for task, url, filePath in downloadTasks:
            if task.result():
                allDownloadedFiles.append((url, filePath))
                count += 1

        # Pause between API page requests
        time.sleep(0.1)

    attributionFile.write("\n")
    print(f"Finished downloading {count} images for {speciesName}.")
    return allDownloadedFiles

# Split images into train and validation sets
def splitTrainingAndValidation(speciesFiles):
    splitMapping = {}  # {speciesName: {"train": [urls], "val": [urls]}}

    for speciesName, files in speciesFiles.items():
        if not files:
            continue

        trainFiles = files.copy()
        numVal = max(1, int(len(files) * VAL_SPLIT))
        valFiles = random.sample(trainFiles, numVal)

        # prepare folders
        valSpeciesFolder = os.path.join(VAL_DIR, speciesName)
        os.makedirs(valSpeciesFolder, exist_ok=True)

        splitMapping[speciesName] = {"train": [], "val": []}

        for url, filePath in files:
            fileName = os.path.basename(filePath)
            if (url, filePath) in valFiles:
                # move file to val
                destination_path = os.path.join(valSpeciesFolder, fileName)
                shutil.move(filePath, destination_path)
                splitMapping[speciesName]["val"].append(url)
            else:
                splitMapping[speciesName]["train"].append(url)

        print(f"Moved {len(valFiles)} images from {speciesName} to validation set.")

    return splitMapping

# Write all URLs into ImageSources.txt with train/val prefixes
def logUrls(splitMapping, logPath):
    with open(logPath, "a") as f:
        for speciesName, sets in splitMapping.items():
            f.write(f"=== {speciesName} ===\n")
            for url in sets["train"]:
                f.write(f"train: {url}\n")
            for url in sets["val"]:
                f.write(f"val: {url}\n")
            f.write("\n")

# Create classnames.txt in train and val folders containing species names
def writeClassnames(trainDir, valDir, speciesList):
    classnames = [name for name, _ in speciesList]
    for folder in [trainDir, valDir]:
        with open(os.path.join(folder, "classnames.txt"), "w") as f:
            f.write("\n".join(classnames))
    print("classnames.txt created in train and val folders.")

if __name__ == "__main__":
    os.makedirs(TRAIN_DIR, exist_ok=True)
    os.makedirs(VAL_DIR, exist_ok=True)
    os.makedirs(SOURCES_DIR, exist_ok=True)

    # Clear previous logs
    if os.path.exists(URLS_LOG_PATH):
        os.remove(URLS_LOG_PATH)
    if os.path.exists(ATTRIBUTIONS_PATH):
        os.remove(ATTRIBUTIONS_PATH)

    speciesFiles = {}

    with open(ATTRIBUTIONS_PATH, "w", encoding="utf-8") as attrFile:
        for species in speciesList:
            downloadedFiles = processSpecies(species, attrFile)
            speciesName, _ = species
            speciesFiles[speciesName] = downloadedFiles

    print("Download phase complete.")

    splitMapping = splitTrainingAndValidation(speciesFiles)
    logUrls(splitMapping, URLS_LOG_PATH)

    writeClassnames(TRAIN_DIR, VAL_DIR, speciesList)

    print("All processing finished.")

    GLOBAL_EXECUTOR.shutdown(wait=True)
