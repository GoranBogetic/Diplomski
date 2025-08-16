import os
import requests
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
MAX_IMAGES_PER_SPECIES = 800
MAX_PAGES = 20
VAL_SPLIT = 0.125
THREADS_PER_SPECIES = 200  # parallel downloads per species

URLS_LOG_PATH = os.path.join(SOURCES_DIR, "ImageSources.txt")


# ==== API HELPER ====
def fetch_from_page(taxon_id, licenses, page, per_page):
    """Fetch one page of observations from iNaturalist API."""
    url = "https://api.inaturalist.org/v1/observations"
    params = {
        "taxon_id": taxon_id,
        "photo_license": ",".join(licenses),
        "per_page": per_page,
        "page": page
    }
    response = requests.get(url, params=params, timeout=15)
    response.raise_for_status()
    return response.json()


# ==== DOWNLOAD HELPER ====
def download_image(url, save_path):
    """Download an image from a URL and save it."""
    try:
        img_data = requests.get(url, timeout=15).content
        with open(save_path, 'wb') as handler:
            handler.write(img_data)
        return True
    except Exception as e:
        print(f"Failed to save {url}: {e}")
        return False


# ==== SPECIES PROCESSING ====
def process_species(species):
    """Download all images for one species using threads."""
    species_name, taxon_id = species
    species_folder = os.path.join(TRAIN_DIR, species_name)
    os.makedirs(species_folder, exist_ok=True)

    count = 0
    all_downloaded_files = []

    for page in range(1, MAX_PAGES + 1):
        if count >= MAX_IMAGES_PER_SPECIES:
            print(f"Reached max images for {species_name}")
            break

        try:
            data = fetch_from_page(taxon_id, LICENSES, page, PER_PAGE)
        except Exception as e:
            print(f"Error fetching page {page} for {species_name}: {e}")
            break

        results = data.get('results', [])
        if not results:
            break

        download_tasks = []
        with ThreadPoolExecutor(max_workers=THREADS_PER_SPECIES) as executor:
            for obs in results:
                for photo in obs.get('photos', []):
                    if count + len(download_tasks) >= MAX_IMAGES_PER_SPECIES:
                        break
                    photo_url = photo['url'].replace('square', 'original')
                    file_name = f"{species_name}_{count + len(download_tasks)}.jpg"
                    file_path = os.path.join(species_folder, file_name)
                    download_tasks.append(
                        (executor.submit(download_image, photo_url, file_path), photo_url, file_path)
                    )

            for future, url, file_path in download_tasks:
                if future.result():
                    all_downloaded_files.append((url, file_path))
                    count += 1

    print(f"Finished downloading {count} images for {species_name}.")
    return all_downloaded_files


# ==== TRAIN/VAL SPLIT ====
def split_training_and_validation(species_files):
    """Split images into train and val folders and return mapping for logging."""
    split_mapping = {}  # {species_name: {"train": [urls], "val": [urls]}}

    for species_name, files in species_files.items():
        if not files:
            continue

        train_files = files.copy()
        num_val = max(1, int(len(files) * VAL_SPLIT))
        val_files = random.sample(train_files, num_val)

        # prepare folders
        val_species_folder = os.path.join(VAL_DIR, species_name)
        os.makedirs(val_species_folder, exist_ok=True)

        split_mapping[species_name] = {"train": [], "val": []}

        for url, file_path in files:
            file_name = os.path.basename(file_path)
            if (url, file_path) in val_files:
                # move file to val
                destination_path = os.path.join(val_species_folder, file_name)
                shutil.move(file_path, destination_path)
                split_mapping[species_name]["val"].append(url)
            else:
                split_mapping[species_name]["train"].append(url)

        print(f"Moved {len(val_files)} images from {species_name} to validation set.")

    return split_mapping


# ==== LOGGING ====
def log_urls(split_mapping, log_path):
    """Write all URLs into ImageSources.txt with train/val prefixes."""
    with open(log_path, "a") as f:
        for species_name, sets in split_mapping.items():
            f.write(f"=== {species_name} ===\n")
            for url in sets["train"]:
                f.write(f"train: {url}\n")
            for url in sets["val"]:
                f.write(f"val: {url}\n")
            f.write("\n")


# ==== MAIN ====
if __name__ == "__main__":
    os.makedirs(TRAIN_DIR, exist_ok=True)
    os.makedirs(VAL_DIR, exist_ok=True)
    os.makedirs(SOURCES_DIR, exist_ok=True)

    # Clear previous log
    if os.path.exists(URLS_LOG_PATH):
        os.remove(URLS_LOG_PATH)

    species_files = {}

    # Download images species by species
    for species in speciesList:
        downloaded_files = process_species(species)
        species_name, _ = species
        species_files[species_name] = downloaded_files

    print("Download phase complete.")

    # Split into train/val and log
    split_mapping = split_training_and_validation(species_files)
    log_urls(split_mapping, URLS_LOG_PATH)

    print("All processing finished.")
