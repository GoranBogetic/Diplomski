import os
from SpeciesList import speciesList

databaseDirectories = [
    r"C:\Users\goran\Desktop\Diplomski\NewDatabase2\train",
    r"C:\Users\goran\Desktop\Diplomski\NewDatabase2\val"
]

for base_dir in databaseDirectories:
    for species_name, _ in speciesList:  # ignore the number part
        folder_path = os.path.join(base_dir, species_name)
        os.makedirs(folder_path, exist_ok=True)
    
    species_file_path = os.path.join(base_dir, "classname.txt")
    with open(species_file_path, "w") as f:
        for species_name, _ in speciesList:
            f.write(species_name + "\n")

    print(f"Created folders and classname.txt in {base_dir}")
