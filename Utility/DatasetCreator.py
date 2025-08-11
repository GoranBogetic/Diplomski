import os
import requests
import time
import random
import shutil
from SpeciesList import speciesList

# ==== CONFIGURATION ====
BASE_DIR = os.path.dirname(__file__)
DATABASE_DIR = os.path.join(BASE_DIR, '../Database')
SOURCES_DIR = os.path.join(BASE_DIR, '../Sources')

# Folders for datasets
TRAIN_DIR = os.path.join(DATABASE_DIR, 'train')
VAL_DIR = os.path.join(DATABASE_DIR, 'val')

# iNaturalist API config
LICENSES = ["cc0", "cc-by", "cc-by-sa"]
PER_PAGE = 200
MAX_IMAGES_PER_SPECIES = 500
MAX_PAGES = 10  # Max pages to query per species to limit requests
VAL_SPLIT = 0.15  # 15% for validation

def featchFromPage(taxonId, licenses, page, perPage):
    """Fetch observations from the iNaturalist API."""
    url = "https://api.inaturalist.org/v1/observations"
    params = {
        "taxon_id": taxonId,
        "photo_license": ",".join(licenses),
        "per_page": perPage,
        "page": page
    }
    response = requests.get(url, params=params)
    response.raise_for_status()
    return response.json()

def downloadImage(url, savePath):
    """Download an image from a URL and save it to disk."""
    try:
        imgData = requests.get(url).content
        with open(savePath, 'wb') as handler:
            handler.write(imgData)
        return True
    except Exception as e:
        print(f"Failed to save {url}: {e}")
        return False

# Ensure directories exist
os.makedirs(TRAIN_DIR, exist_ok=True)
os.makedirs(VAL_DIR, exist_ok=True)
os.makedirs(SOURCES_DIR, exist_ok=True)

# Path for the single URL log file (now in SOURCES_DIR)
allUrlsFilePath = os.path.join(SOURCES_DIR, "ImageSources.txt")

# ==== DOWNLOAD PHASE ====
with open(allUrlsFilePath, 'w') as allUrlsFile:
    for speciesName, taxonId in speciesList:
        # Create folder for the species in train
        speciesFolder = os.path.join(TRAIN_DIR, speciesName)
        os.makedirs(speciesFolder, exist_ok=True)

        count = 0
        allUrlsFile.write(f"=== {speciesName} ===\n")  # Species header

        for page in range(1, MAX_PAGES + 1):
            if count >= MAX_IMAGES_PER_SPECIES:
                print(f"Reached max images ({MAX_IMAGES_PER_SPECIES}) for {speciesName}")
                break
            try:
                data = featchFromPage(taxonId, LICENSES, page, PER_PAGE)
            except Exception as e:
                print(f"Error fetching page {page} for {speciesName}: {e}")
                break

            results = data.get('results', [])
            if not results:
                print(f"No more results for {speciesName} on page {page}")
                break

            for obs in results:
                photos = obs.get('photos', [])
                for photo in photos:
                    if count >= MAX_IMAGES_PER_SPECIES:
                        break
                    # Get original-sized image URL
                    photoUrl = photo['url'].replace('square', 'original')
                    fileName = f"{speciesName}{count}.jpg"
                    filePath = os.path.join(speciesFolder, fileName)

                    if downloadImage(photoUrl, filePath):
                        allUrlsFile.write(photoUrl + '\n')
                        count += 1

            # Respect API rate limits
            time.sleep(1)

        allUrlsFile.write("\n")  # Blank line after each species
        print(f"Finished downloading {count} images for {speciesName}.\n")

print("Download phase complete.")

# ==== SPLIT PHASE ====
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

print("Train/val split complete. All processing finished.")
