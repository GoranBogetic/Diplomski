import os
import requests
import random
import shutil
from concurrent.futures import ThreadPoolExecutor
from SpeciesList import speciesList

# ==== CONFIGURATION ====
BASE_DIR = os.path.dirname(__file__)
DATABASE_DIR = os.path.join(BASE_DIR, '../Database')
SOURCES_DIR = os.path.join(BASE_DIR, '../Sources')

TRAIN_DIR = os.path.join(DATABASE_DIR, 'train')
VAL_DIR = os.path.join(DATABASE_DIR, 'val')

LICENSES = ["cc0", "cc-by", "cc-by-sa"]
PER_PAGE = 200
MAX_IMAGES_PER_SPECIES = 800
MAX_PAGES = 20
VAL_SPLIT = 0.125
THREADS_PER_SPECIES = 200  # parallel downloads per species

URLS_LOG_PATH = os.path.join(SOURCES_DIR, "ImageSources.txt")


# ==== API HELPER ====
def fetchFromPage(taxonId, licenses, page, perPage):
    """Fetch one page of observations from iNaturalist API."""
    url = "https://api.inaturalist.org/v1/observations"
    params = {
        "taxonId": taxonId,
        "photo_license": ",".join(licenses),
        "per_page": perPage,
        "page": page
    }
    response = requests.get(url, params=params, timeout=15)
    response.raise_for_status()
    return response.json()


# ==== DOWNLOAD HELPER ====
def downloadImage(url, savePath):
    """Download an image from a URL and save it."""
    try:
        imgData = requests.get(url, timeout=15).content
        with open(savePath, 'wb') as handler:
            handler.write(imgData)
        return True
    except Exception as e:
        print(f"Failed to save {url}: {e}")
        return False


# ==== SPECIES PROCESSING ====
def processSpecies(species):
    """Download all images for one species using threads."""
    speciesName, taxonId = species
    speciesFolder = os.path.join(TRAIN_DIR, speciesName)
    os.makedirs(speciesFolder, exist_ok=True)

    count = 0
    allDownloadedFiles = []

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
        with ThreadPoolExecutor(max_workers=THREADS_PER_SPECIES) as executor:
            for obs in results:
                for photo in obs.get('photos', []):
                    if count + len(downloadTasks) >= MAX_IMAGES_PER_SPECIES:
                        break
                    photoUrl = photo['url'].replace('square', 'original')
                    fileName = f"{speciesName}_{count + len(downloadTasks)}.jpg"
                    filePath = os.path.join(speciesFolder, fileName)
                    downloadTasks.append(
                        (executor.submit(downloadImage, photoUrl, filePath), photoUrl, filePath)
                    )

            for future, url, filePath in downloadTasks:
                if future.result():
                    allDownloadedFiles.append((url, filePath))
                    count += 1

    print(f"Finished downloading {count} images for {speciesName}.")
    return allDownloadedFiles


# ==== TRAIN/VAL SPLIT ====
def splitTrainingAndValidation(speciesFiles):
    """Split images into train and val folders and return mapping for logging."""
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


# ==== LOGGING ====
def logUrls(splitMapping, logPath):
    """Write all URLs into ImageSources.txt with train/val prefixes."""
    with open(logPath, "a") as f:
        for speciesName, sets in splitMapping.items():
            f.write(f"=== {speciesName} ===\n")
            for url in sets["train"]:
                f.write(f"train: {url}\n")
            for url in sets["val"]:
                f.write(f"val: {url}\n")
            f.write("\n")


# ==== CLASSNAMES FILE CREATOR ====
def writeClassnames(trainDir, valDir, speciesList):
    """Create classnames.txt in train and val folders containing species names."""
    classnames = [name for name, _ in speciesList]
    for folder in [trainDir, valDir]:
        with open(os.path.join(folder, "classnames.txt"), "w") as f:
            f.write("\n".join(classnames))
    print("classnames.txt created in train and val folders.")


# ==== MAIN ====
if __name__ == "__main__":
    os.makedirs(TRAIN_DIR, exist_ok=True)
    os.makedirs(VAL_DIR, exist_ok=True)
    os.makedirs(SOURCES_DIR, exist_ok=True)

    # Clear previous log
    if os.path.exists(URLS_LOG_PATH):
        os.remove(URLS_LOG_PATH)

    speciesFiles = {}

    # Download images species by species
    for species in speciesList:
        downloadedFiles = processSpecies(species)
        speciesName, _ = species
        speciesFiles[speciesName] = downloadedFiles

    print("Download phase complete.")

    # Split into train/val and log
    splitMapping = splitTrainingAndValidation(speciesFiles)
    logUrls(splitMapping, URLS_LOG_PATH)

    # Write classnames.txt files
    writeClassnames(TRAIN_DIR, VAL_DIR, speciesList)

    print("All processing finished.")
