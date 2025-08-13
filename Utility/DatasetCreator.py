import os
import requests
import time
import random
import shutil
from concurrent.futures import ThreadPoolExecutor, as_completed
from SpeciesList import speciesList

# ==== CONFIGURATION ====
BASE_DIR = os.path.dirname(__file__)
DATABASE_DIR = os.path.join(BASE_DIR, '../Database')
SOURCES_DIR = os.path.join(BASE_DIR, '../Sources')

TRAIN_DIR = os.path.join(DATABASE_DIR, 'train')
VAL_DIR = os.path.join(DATABASE_DIR, 'val')

LICENSES = ["cc0", "cc-by", "cc-by-sa"]
PER_PAGE = 200
MAX_IMAGES_PER_SPECIES = 500
MAX_PAGES = 10
VAL_SPLIT = 0.15
THREADS_PER_SPECIES = 20  # how many images to download in parallel per species

# Shared log file for all URLs
URLS_LOG_PATH = os.path.join(SOURCES_DIR, "ImageSources.txt")


def featchFromPage(taxonId, licenses, page, perPage):
    url = "https://api.inaturalist.org/v1/observations"
    params = {
        "taxonId": taxonId,
        "photoLicense": ",".join(licenses),
        "perPage": perPage,
        "page": page
    }
    response = requests.get(url, params=params, timeout=15)
    response.raise_for_status()
    return response.json()


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


def processSpecies(species):
    """Download all images for one species using threads."""
    speciesName, taxonId = species
    speciesFolder = os.path.join(TRAIN_DIR, speciesName)
    os.makedirs(speciesFolder, exist_ok=True)

    count = 0

    with open(URLS_LOG_PATH, 'a') as urlsLog:
        urlsLog.write(f"=== {speciesName} ===\n")

        for page in range(1, MAX_PAGES + 1):
            if count >= MAX_IMAGES_PER_SPECIES:
                print(f"Reached max images for {speciesName}")
                break

            try:
                data = featchFromPage(taxonId, LICENSES, page, PER_PAGE)
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
                            (executor.submit(downloadImage, photoUrl, filePath), photoUrl)
                        )

                for future, url in downloadTasks:
                    if future.result():
                        urlsLog.write(url + '\n')
                        count += 1

        urlsLog.write("\n")

    print(f"Finished downloading {count} images for {speciesName}.")


def splitTrainingAndValidation():
    print("Starting train/val split...")
    for speciesName in os.listdir(TRAIN_DIR):
        speciesFolder = os.path.join(TRAIN_DIR, speciesName)
        if not os.path.isdir(speciesFolder):
            continue

        images = [f for f in os.listdir(speciesFolder) if f.lower().endswith(('.jpg', '.jpeg', '.png'))]
        if not images:
            continue

        numVal = max(1, int(len(images) * VAL_SPLIT))
        valImages = random.sample(images, numVal)

        valSpeciesFolder = os.path.join(VAL_DIR, speciesName)
        os.makedirs(valSpeciesFolder, exist_ok=True)

        for imgName in valImages:
            sourcePath = os.path.join(speciesFolder, imgName)
            destinationPath = os.path.join(valSpeciesFolder, imgName)
            shutil.move(sourcePath, destinationPath)

        print(f"Moved {numVal} images from {speciesName} to validation set.")
    print("Train/val split complete.")


if __name__ == "__main__":
    os.makedirs(TRAIN_DIR, exist_ok=True)
    os.makedirs(VAL_DIR, exist_ok=True)
    os.makedirs(SOURCES_DIR, exist_ok=True)

    # Clear previous log file if exists
    if os.path.exists(URLS_LOG_PATH):
        os.remove(URLS_LOG_PATH)

    for species in speciesList:
        processSpecies(species)  # sequential per species, but threaded inside

    print("Download phase complete.")
    splitTrainingAndValidation()
    print("All processing finished.")
