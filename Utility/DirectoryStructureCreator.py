import os
from SpeciesList import speciesList

# Base directories relative to the script location
BASE_DIR = os.path.dirname(__file__)
DATABASE_DIR = os.path.join(BASE_DIR, '../Database')

# Train and validation dataset directories
databaseDirectories = [
    os.path.join(DATABASE_DIR, 'train'),
    os.path.join(DATABASE_DIR, 'val')
]

for baseDir in databaseDirectories:
    os.makedirs(baseDir, exist_ok=True)

    # Create species folders
    for speciesName, _ in speciesList:
        folderPath = os.path.join(baseDir, speciesName)
        os.makedirs(folderPath, exist_ok=True)
    
    # Create classname.txt
    speciesFilePath = os.path.join(baseDir, "classname.txt")
    with open(speciesFilePath, "w") as f:
        for speciesName, _ in speciesList:
            f.write(speciesName + "\n")

    print(f"Created folders and classname.txt in {baseDir}")
