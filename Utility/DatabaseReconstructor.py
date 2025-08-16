import os
import requests
from concurrent.futures import ThreadPoolExecutor, as_completed

# ==== CONFIGURATION ====
BASE_DIR = os.path.dirname(__file__)
DATABASE_DIR = os.path.join(BASE_DIR, '../Database')
SOURCES_DIR = os.path.join(BASE_DIR, '../Sources')

TRAIN_DIR = os.path.join(DATABASE_DIR, 'train')
VAL_DIR = os.path.join(DATABASE_DIR, 'val')

THREADS_PER_SPECIES = 200
URLS_LOG_PATH = os.path.join(SOURCES_DIR, "ImageSources.txt")


# ==== DOWNLOAD HELPERS ====
def download_image(url, save_path):
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


def download_species_images(species_name, urls, out_dir, threads):
    """Download all URLs for a species in parallel."""
    os.makedirs(out_dir, exist_ok=True)
    count = 0

    with ThreadPoolExecutor(max_workers=threads) as executor:
        future_to_url = {}
        for idx, url in enumerate(urls):
            file_name = f"{species_name}_{idx}.jpg"
            file_path = os.path.join(out_dir, file_name)
            if os.path.exists(file_path):
                continue
            future = executor.submit(download_image, url, file_path)
            future_to_url[future] = url

        for future in as_completed(future_to_url):
            url = future_to_url[future]
            if future.result():
                count += 1

    print(f"Downloaded {count} images for {species_name}")
    return count


# ==== PARSING HELPER ====
def parse_image_sources(file_path):
    """Parse ImageSources.txt into a dict: {species_name: [urls]}"""
    species_dict = {}
    current_species = None
    if not os.path.exists(file_path):
        print(f"{file_path} does not exist!")
        return species_dict

    with open(file_path, "r") as f:
        for line in f:
            line = line.strip()
            if line.startswith("===") and line.endswith("==="):
                current_species = line.strip("= ").strip()
                species_dict[current_species] = []
            elif line and current_species:
                species_dict[current_species].append(line)
    return species_dict


# ==== MAIN RECONSTRUCTION ====
def reconstruct_database():
    os.makedirs(TRAIN_DIR, exist_ok=True)
    species_urls = parse_image_sources(URLS_LOG_PATH)

    for species_name, urls in species_urls.items():
        print(f"Reconstructing {species_name} with {len(urls)} images...")
        species_folder = os.path.join(TRAIN_DIR, species_name)
        download_species_images(species_name, urls, species_folder, THREADS_PER_SPECIES)

    print("Database reconstruction complete.")


if __name__ == "__main__":
    reconstruct_database()
