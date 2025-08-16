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
THREADS_PER_SPECIES = 200  # how many images to download in parallel per species

URLS_LOG_PATH = os.path.join(SOURCES_DIR, "ImageSources.txt")


# ==== API HELPERS ====
def fetch_from_page(taxon_id, licenses, page, per_page):
    """Fetch one page of observations from iNaturalist API (no quality filter)."""
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


def collect_image_urls(taxon_id, max_images, max_pages, licenses, per_page):
    """Collect image URLs for a species up to limits."""
    urls = []
    for page in range(1, max_pages + 1):
        if len(urls) >= max_images:
            break
        try:
            data = fetch_from_page(taxon_id, licenses, page, per_page)
        except Exception as e:
            print(f"Error fetching page {page} for taxon {taxon_id}: {e}")
            break

        results = data.get("results", [])
        if not results:
            break

        for obs in results:
            for photo in obs.get("photos", []):
                if len(urls) >= max_images:
                    break
                # use original size instead of square
                photo_url = photo["url"].replace("square", "original")
                urls.append(photo_url)

    return urls


# ==== DOWNLOAD HELPERS ====
def download_image(url, save_path):
    """Download an image from a URL and save it to disk."""
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


def download_images_parallel(urls, species_name, out_dir, threads, log_file):
    """Download multiple images in parallel and log successful URLs."""
    os.makedirs(out_dir, exist_ok=True)
    count = 0

    with open(log_file, "a") as log:
        log.write(f"=== {species_name} ===\n")

        with ThreadPoolExecutor(max_workers=threads) as executor:
            future_to_url = {}
            for idx, url in enumerate(urls):
                file_name = f"{species_name}_{idx}.jpg"
                file_path = os.path.join(out_dir, file_name)
                if os.path.exists(file_path):  # skip if already downloaded
                    continue
                future = executor.submit(download_image, url, file_path)
                future_to_url[future] = url

            for future in as_completed(future_to_url):
                url = future_to_url[future]
                if future.result():
                    log.write(url + "\n")
                    count += 1

        log.write("\n")

    return count


# ==== SPECIES PROCESSING ====
def process_species(species, max_images=MAX_IMAGES_PER_SPECIES):
    """Process one species: collect URLs and download images."""
    species_name, taxon_id = species
    print(f"Processing {species_name}...")

    # Stage 1: collect URLs
    urls = collect_image_urls(
        taxon_id,
        max_images,
        MAX_PAGES,
        LICENSES,
        PER_PAGE
    )
    print(f"Collected {len(urls)} image URLs for {species_name}")

    # Stage 2: download
    species_folder = os.path.join(TRAIN_DIR, species_name)
    count = download_images_parallel(
        urls, species_name, species_folder, THREADS_PER_SPECIES, URLS_LOG_PATH
    )
    print(f"Downloaded {count} images for {species_name}")


# ==== DATASET HELPERS ====
def split_training_and_validation(val_split=VAL_SPLIT):
    """Split existing training data into train/val sets."""
    print("Starting train/val split...")
    for species_name in os.listdir(TRAIN_DIR):
        species_folder = os.path.join(TRAIN_DIR, species_name)
        if not os.path.isdir(species_folder):
            continue

        images = [f for f in os.listdir(species_folder)
                  if f.lower().endswith(('.jpg', '.jpeg', '.png'))]
        if not images:
            continue

        num_val = max(1, int(len(images) * val_split))
        val_images = random.sample(images, num_val)

        val_species_folder = os.path.join(VAL_DIR, species_name)
        os.makedirs(val_species_folder, exist_ok=True)

        for img_name in val_images:
            src = os.path.join(species_folder, img_name)
            dst = os.path.join(val_species_folder, img_name)
            shutil.move(src, dst)

        print(f"Moved {num_val} images from {species_name} to validation set.")

    print("Train/val split complete.")


# ==== MAIN PIPELINE ====
def main():
    os.makedirs(TRAIN_DIR, exist_ok=True)
    os.makedirs(VAL_DIR, exist_ok=True)
    os.makedirs(SOURCES_DIR, exist_ok=True)

    if os.path.exists(URLS_LOG_PATH):
        os.remove(URLS_LOG_PATH)

    for species in speciesList:
        process_species(species)

    print("Download phase complete.")
    split_training_and_validation()
    print("All processing finished.")


if __name__ == "__main__":
    main()
