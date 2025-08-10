import os
import requests
import time
from SpeciesList import speciesList

# ==== CONFIGURATION ====
BASE_SAVE_DIR = r"C:\Users\goran\Desktop\Diplomski\NewDatabase2\train"  # Base folder for all species
LICENSES = ["cc0", "cc-by", "cc-by-sa"]
PER_PAGE = 200
MAX_IMAGES_PER_SPECIES = 500
MAX_PAGES = 10  # Max pages to query per species to limit requests

def get_observations(taxon_id, licenses, page, per_page):
    url = "https://api.inaturalist.org/v1/observations"
    params = {
        "taxon_id": taxon_id,
        "photo_license": ",".join(licenses),
        "per_page": per_page,
        "page": page
    }
    response = requests.get(url, params=params)
    response.raise_for_status()
    return response.json()

def download_image(url, save_path):
    try:
        img_data = requests.get(url).content
        with open(save_path, 'wb') as handler:
            handler.write(img_data)
        return True
    except Exception as e:
        print(f"Failed to save {url}: {e}")
        return False

for species_name, taxon_id in speciesList:
    species_folder = os.path.join(BASE_SAVE_DIR, species_name)
    os.makedirs(species_folder, exist_ok=True)
    urls_file_path = os.path.join(species_folder, f"{species_name}_image_urls.txt")

    count = 0
    with open(urls_file_path, 'w') as urls_file:
        for page in range(1, MAX_PAGES + 1):
            if count >= MAX_IMAGES_PER_SPECIES:
                print(f"Reached max images ({MAX_IMAGES_PER_SPECIES}) for {species_name}")
                break
            try:
                data = get_observations(taxon_id, LICENSES, page, PER_PAGE)
            except Exception as e:
                print(f"Error fetching page {page} for {species_name}: {e}")
                break

            results = data.get('results', [])
            if not results:
                print(f"No more results for {species_name} on page {page}")
                break

            for obs in results:
                photos = obs.get('photos', [])
                for photo in photos:
                    if count >= MAX_IMAGES_PER_SPECIES:
                        break
                    photo_url = photo['url'].replace('square', 'original')
                    file_name = f"{species_name}{count}.jpg"
                    file_path = os.path.join(species_folder, file_name)
                    if download_image(photo_url, file_path):
                        urls_file.write(photo_url + '\n')
                        count += 1
            time.sleep(1)

    print(f"Finished downloading {count} images for {species_name}.\n")

print("All species processed.")
