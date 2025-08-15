package com.example.plantfinder

data class PlantData(
    val englishName: String,
    val croatianName: String,
    val latinName: String,
    val description: String,
    val isEdible: Boolean
)

val plantInfo = mapOf(
    "aleppopine" to PlantData(
        englishName = "Aleppo Pine",
        croatianName = "Alepski Bor",
        latinName = "Pinus halepensis",
        description = "A Mediterranean pine tree with slender branches and long needles. Often used in reforestation and ornamental planting.",
        isEdible = false
    ),
    "europeanash" to PlantData(
        englishName = "European Ash",
        croatianName = "Jasen",
        latinName = "Fraxinus excelsior",
        description = "A tall deciduous tree with compound leaves and winged seeds. Wood is strong and commonly used for furniture.",
        isEdible = false
    ),
    "aloevera" to PlantData(
        englishName = "Aloe Vera",
        croatianName = "Aloe Vera",
        latinName = "Aloe vera",
        description = "A succulent plant with thick, fleshy leaves containing gel. Widely used for skin care and minor burns.",
        isEdible = true
    ),
    "banana" to PlantData(
        englishName = "Banana",
        croatianName = "Banana",
        latinName = "Musa spp.",
        description = "A tropical herbaceous plant producing elongated, sweet fruits. Staple fruit in many regions.",
        isEdible = true
    ),
    "baylaurel" to PlantData(
        englishName = "Bay Laurel",
        croatianName = "Lovor",
        latinName = "Laurus nobilis",
        description = "An aromatic evergreen shrub with glossy leaves. Leaves are commonly used in cooking.",
        isEdible = true
    ),
    "bilimbi" to PlantData(
        englishName = "Bilimbi",
        croatianName = "Bilimbi",
        latinName = "Averrhoa bilimbi",
        description = "A small tropical tree producing sour, cucumber-shaped fruits. Often used in pickles and preserves.",
        isEdible = true
    ),
    "blackpine" to PlantData(
        englishName = "Black Pine",
        croatianName = "Crni Bor",
        latinName = "Pinus nigra",
        description = "A large pine tree with dark bark and long needles. Popular in parks and forestry.",
        isEdible = false
    ),
    "blacklocust" to PlantData(
        englishName = "Black Locust",
        croatianName = "Crni Bagrem",
        latinName = "Robinia pseudoacacia",
        description = "A hardy deciduous tree with fragrant flowers. Flowers are edible; other parts are toxic.",
        isEdible = true
    ),
    "bladdersenna" to PlantData(
        englishName = "Bladder Senna",
        croatianName = "Balonasta Sjena",
        latinName = "Colutea arborescens",
        description = "A shrub with yellow flowers and inflated seed pods. Mostly ornamental, not commonly eaten.",
        isEdible = false
    ),
    "bougainvillea" to PlantData(
        englishName = "Bougainvillea",
        croatianName = "Bugenvilija",
        latinName = "Bougainvillea spp.",
        description = "A climbing plant with colorful bracts surrounding small flowers. Ornamental, not edible.",
        isEdible = false
    ),
    "canaryislanddatepalm" to PlantData(
        englishName = "Canary Island Date Palm",
        croatianName = "Kanarska Datulja",
        latinName = "Phoenix canariensis",
        description = "A large palm with long arching fronds. Cultivated for ornamental use; fruits are edible but not common.",
        isEdible = true
    ),
    "Cantaloupe" to PlantData(
        englishName = "Cantaloupe",
        croatianName = "Kantalupa",
        latinName = "Cucumis melo",
        description = "A melon with sweet orange flesh and netted skin. Widely eaten fresh as fruit.",
        isEdible = true
    ),
    "carobtree" to PlantData(
        englishName = "Carob Tree",
        croatianName = "Rovnica",
        latinName = "Ceratonia siliqua",
        description = "An evergreen tree producing pods used as a chocolate substitute. Pods are edible and sweet.",
        isEdible = true
    ),
    "cassava" to PlantData(
        englishName = "Cassava",
        croatianName = "Kassava",
        latinName = "Manihot esculenta",
        description = "A tropical root crop with starchy tubers. Must be cooked to remove toxins before eating.",
        isEdible = true
    ),
    "cherrytree" to PlantData(
        englishName = "Cherry Tree",
        croatianName = "Trešnja",
        latinName = "Prunus avium",
        description = "A deciduous tree producing sweet red fruits. Fruits are eaten fresh or used in desserts.",
        isEdible = true
    ),
    "chestnuttree" to PlantData(
        englishName = "Chestnut Tree",
        croatianName = "Kesten",
        latinName = "Castanea sativa",
        description = "A large deciduous tree with spiny fruits containing nuts. Nuts are edible when cooked.",
        isEdible = true
    ),
    "coconut" to PlantData(
        englishName = "Coconut",
        croatianName = "Kokos",
        latinName = "Cocos nucifera",
        description = "A tropical palm producing large fibrous fruits. The water and flesh are edible and widely consumed.",
        isEdible = true
    ),
    "commonfigtree" to PlantData(
        englishName = "Common Fig Tree",
        croatianName = "Figa",
        latinName = "Ficus carica",
        description = "A small deciduous tree producing sweet figs. Fruits are eaten fresh or dried.",
        isEdible = true
    ),
    "commonpoppy" to PlantData(
        englishName = "Common Poppy",
        croatianName = "Mak",
        latinName = "Papaver rhoeas",
        description = "A delicate annual flower with red petals. Seeds are edible; plant is mainly ornamental.",
        isEdible = true
    ),
    "commonsage" to PlantData(
        englishName = "Common Sage",
        croatianName = "Kadulja",
        latinName = "Salvia officinalis",
        description = "A perennial herb with aromatic gray-green leaves. Leaves are edible and used in cooking.",
        isEdible = true
    ),
    "corn" to PlantData(
        englishName = "Corn",
        croatianName = "Kukuruz",
        latinName = "Zea mays",
        description = "A tall cereal plant producing ears with kernels. Kernels are widely eaten fresh, cooked, or ground into flour.",
        isEdible = true
    ),
    "coriander" to PlantData(
        englishName = "Coriander",
        croatianName = "Korijander",
        latinName = "Coriandrum sativum",
        description = "An annual herb with aromatic leaves and seeds. Leaves and seeds are edible and used in cooking.",
        isEdible = true
    ),
    "cucumber" to PlantData(
        englishName = "Cucumber",
        croatianName = "Krastavac",
        latinName = "Cucumis sativus",
        description = "A climbing plant producing long green fruits. Fruits are edible and commonly eaten raw.",
        isEdible = true
    ),
    "curcuma" to PlantData(
        englishName = "Turmeric",
        croatianName = "Kurkuma",
        latinName = "Curcuma longa",
        description = "A tropical plant with aromatic rhizomes. Rhizomes are edible and used as a spice.",
        isEdible = true
    ),
    "cypresstree" to PlantData(
        englishName = "Cypress Tree",
        croatianName = "Čempres",
        latinName = "Cupressus spp.",
        description = "An evergreen tree with conical shape and scale-like leaves. Primarily ornamental.",
        isEdible = false
    ),
    "crowndaisy" to PlantData(
        englishName = "Crown Daisy",
        croatianName = "Krizantema",
        latinName = "Glebionis coronaria",
        description = "An annual plant with yellow daisy-like flowers. Leaves are edible and used in salads.",
        isEdible = true
    ),
    "calmatianbellflower" to PlantData(
        englishName = "Dalmatian Bellflower",
        croatianName = "Dalmatinski Zvjezdan",
        latinName = "Campanula portenschlagiana",
        description = "A low-growing perennial with violet bell-shaped flowers. Mainly ornamental.",
        isEdible = false
    ),
    "daphne" to PlantData(
        englishName = "Daphne",
        croatianName = "Dafne",
        latinName = "Daphne spp.",
        description = "A small shrub with fragrant flowers. All parts are toxic, ornamental only.",
        isEdible = false
    ),
    "elderberry" to PlantData(
        englishName = "Elderberry",
        croatianName = "Bazga",
        latinName = "Sambucus nigra",
        description = "A shrub or small tree producing dark berries. Berries are edible when cooked.",
        isEdible = true
    ),
    "eggplant" to PlantData(
        englishName = "Eggplant",
        croatianName = "Patlidžan",
        latinName = "Solanum melongena",
        description = "A perennial plant producing purple fruits. Fruits are edible and widely used in cooking.",
        isEdible = true
    ),
    "elmtree" to PlantData(
        englishName = "Elm Tree",
        croatianName = "Brijest",
        latinName = "Ulmus spp.",
        description = "A large deciduous tree with serrated leaves. Primarily used for shade and timber.",
        isEdible = false
    ),
    "fennel" to PlantData(
        englishName = "Fennel",
        croatianName = "Komorač",
        latinName = "Foeniculum vulgare",
        description = "A perennial herb with feathery leaves and bulbous base. Bulb, leaves, and seeds are edible.",
        isEdible = true
    ),
    "garlic" to PlantData(
        englishName = "Garlic",
        croatianName = "Češnjak",
        latinName = "Allium sativum",
        description = "A bulbous plant with strong-smelling cloves. Widely used as a culinary and medicinal ingredient.",
        isEdible = true
    ),
    "gardenia" to PlantData(
        englishName = "Gardenia",
        croatianName = "Gardenija",
        latinName = "Gardenia jasminoides",
        description = "An evergreen shrub with fragrant white flowers. Ornamental, not edible.",
        isEdible = false
    ),
    "geranium" to PlantData(
        englishName = "Geranium",
        croatianName = "Pelargonija",
        latinName = "Pelargonium spp.",
        description = "A flowering plant with rounded leaves and colorful blooms. Primarily ornamental.",
        isEdible = false
    ),
    "giantfennel" to PlantData(
        englishName = "Giant Fennel",
        croatianName = "Divlji Komorač",
        latinName = "Ferula communis",
        description = "A tall perennial with large umbels of yellow flowers. Not commonly eaten, sometimes medicinal.",
        isEdible = false
    ),
    "goldenrod" to PlantData(
        englishName = "Golden Rod",
        croatianName = "Zlatnica",
        latinName = "Solidago spp.",
        description = "A perennial with bright yellow flower spikes. Mainly ornamental or medicinal.",
        isEdible = false
    ),
    "fuava" to PlantData(
        englishName = "Guava",
        croatianName = "Guava",
        latinName = "Psidium guajava",
        description = "A tropical tree producing sweet, aromatic fruits. Fruits are edible and nutritious.",
        isEdible = true
    ),
    "hackberry" to PlantData(
        englishName = "Hackberry",
        croatianName = "Brest",
        latinName = "Celtis spp.",
        description = "A deciduous tree with small sweet fruits. Fruits are edible but rarely eaten.",
        isEdible = true
    ),
    "hawthorn" to PlantData(
        englishName = "Hawthorn",
        croatianName = "Glog",
        latinName = "Crataegus spp.",
        description = "A shrub or small tree with white or pink flowers. Fruits are edible and used in jams or teas.",
        isEdible = true
    ),
    "herbrobert" to PlantData(
        englishName = "Herb Robert",
        croatianName = "Gorušica",
        latinName = "Geranium robertianum",
        description = "A small herb with pink flowers. Edible and used in folk medicine.",
        isEdible = true
    ),
    "holmOak" to PlantData(
        englishName = "Holm Oak",
        croatianName = "Hrast Medunac",
        latinName = "Quercus ilex",
        description = "An evergreen oak with leathery leaves. Acorns are edible when processed.",
        isEdible = true
    ),
    "hyssop" to PlantData(
        englishName = "Hyssop",
        croatianName = "Hisop",
        latinName = "Hyssopus officinalis",
        description = "A small aromatic herb with blue flowers. Leaves and flowers are edible and used in cooking.",
        isEdible = true
    ),
    "italianbuckthorn" to PlantData(
        englishName = "Italian Buckthorn",
        croatianName = "Talijanski Ruj",
        latinName = "Rhamnus alaternus",
        description = "An evergreen shrub with dark berries. Mostly ornamental; berries not commonly eaten.",
        isEdible = false
    ),
    "juniper" to PlantData(
        englishName = "Juniper",
        croatianName = "Borovica",
        latinName = "Juniperus communis",
        description = "A coniferous shrub or tree with needle-like leaves. Berries are edible and used as spice.",
        isEdible = true
    ),
    "kermesoak" to PlantData(
        englishName = "Kermes Oak",
        croatianName = "Crni Hrast",
        latinName = "Quercus coccifera",
        description = "A small evergreen oak with spiny leaves. Acorns are edible but bitter.",
        isEdible = true
    ),
    "lavender" to PlantData(
        englishName = "Lavender",
        croatianName = "Lavanda",
        latinName = "Lavandula angustifolia",
        description = "A fragrant herb with purple flowers. Flowers are edible and used in culinary and tea.",
        isEdible = true
    ),
    "lemontree" to PlantData(
        englishName = "Lemon Tree",
        croatianName = "Limun",
        latinName = "Citrus limon",
        description = "A small evergreen tree producing sour yellow fruits. Fruits are edible and widely used in cooking.",
        isEdible = true
    ),
    "lilac" to PlantData(
        englishName = "Lilac",
        croatianName = "Lila",
        latinName = "Syringa vulgaris",
        description = "A deciduous shrub with fragrant purple or white flowers. Mainly ornamental, not eaten.",
        isEdible = false
    ),
    "mango" to PlantData(
        englishName = "Mango",
        croatianName = "Mango",
        latinName = "Mangifera indica",
        description = "A tropical tree producing sweet orange fruits. Fruits are edible and popular worldwide.",
        isEdible = true
    ),
    "mastictree" to PlantData(
        englishName = "Mastic Tree",
        croatianName = "Mastik",
        latinName = "Pistacia lentiscus",
        description = "An evergreen shrub with aromatic resin. Leaves and resin are edible; fruits are small.",
        isEdible = true
    ),
    "mediterraneancypress" to PlantData(
        englishName = "Mediterranean Cypress",
        croatianName = "Sredozemni Čempres",
        latinName = "Cupressus sempervirens",
        description = "A tall, narrow evergreen tree. Mainly ornamental, not eaten.",
        isEdible = false
    ),
    "mexicanfanpalm" to PlantData(
        englishName = "Mexican Fan Palm",
        croatianName = "Meksička Lepezasta Palma",
        latinName = "Washingtonia robusta",
        description = "A fast-growing palm with fan-shaped leaves. Ornamental, fruits not commonly eaten.",
        isEdible = false
    ),
    "mockprivet" to PlantData(
        englishName = "Mock Privet",
        croatianName = "Lažni Liguster",
        latinName = "Phillyrea spp.",
        description = "An evergreen shrub with leathery leaves. Mostly ornamental, not edible.",
        isEdible = false
    ),
    "myrtle" to PlantData(
        englishName = "Myrtle",
        croatianName = "Mirt",
        latinName = "Myrtus communis",
        description = "An evergreen shrub with aromatic leaves and berries. Leaves and berries are edible.",
        isEdible = true
    ),
    "narcissus" to PlantData(
        englishName = "Narcissus",
        croatianName = "Narcis",
        latinName = "Narcissus spp.",
        description = "A bulbous flowering plant with showy flowers. Ornamental, not edible.",
        isEdible = false
    ),
    "nlivetree" to PlantData(
        englishName = "Olive Tree",
        croatianName = "Maslina",
        latinName = "Olea europaea",
        description = "An evergreen tree producing olives. Olives are edible and used for oil and consumption.",
        isEdible = true
    ),
    "orientalplane" to PlantData(
        englishName = "Oriental Plane",
        croatianName = "Planinski Javor",
        latinName = "Platanus orientalis",
        description = "A large deciduous tree with broad leaves. Mainly ornamental, not edible.",
        isEdible = false
    ),
    "oregano" to PlantData(
        englishName = "Oregano",
        croatianName = "Origano",
        latinName = "Origanum vulgare",
        description = "A perennial herb with aromatic leaves, widely used in Mediterranean cuisine. It produces small purple flowers and thrives in sunny, well-drained soils.",
        isEdible = true
    ),
    "orange" to PlantData(
        englishName = "Orange",
        croatianName = "Naranča",
        latinName = "Citrus × sinensis",
        description = "A small evergreen tree producing sweet, juicy citrus fruits. Valued for its vitamin C content and fragrant blossoms.",
        isEdible = true
    ),
    "papaya" to PlantData(
        englishName = "Papaya",
        croatianName = "Papaja",
        latinName = "Carica papaya",
        description = "A fast-growing tropical tree with large lobed leaves and sweet, orange-fleshed fruits. Known for its digestive enzyme papain.",
        isEdible = true
    ),
    "papaversomniferum" to PlantData(
        englishName = "Opium Poppy",
        croatianName = "Opijumski mak",
        latinName = "Papaver somniferum",
        description = "An annual plant with large flowers and seed capsules used for culinary poppy seeds and in pharmaceutical production. Contains alkaloids with medicinal and narcotic properties.",
        isEdible = true
    ),
    "parsley" to PlantData(
        englishName = "Parsley",
        croatianName = "Peršin",
        latinName = "Petroselinum crispum",
        description = "A biennial herb cultivated for its fresh green leaves, widely used as a garnish and seasoning. Also valued for its vitamin and mineral content.",
        isEdible = true
    ),
    "peony" to PlantData(
        englishName = "Peony",
        croatianName = "Božur",
        latinName = "Paeonia officinalis",
        description = "A perennial plant with large, showy flowers in various colors. Grown mainly for ornamental purposes, with some historical medicinal uses.",
        isEdible = false
    ),
    "peperchili" to PlantData(
        englishName = "Chili Pepper",
        croatianName = "Čili paprika",
        latinName = "Capsicum annuum",
        description = "A fruiting plant producing hot or sweet peppers, used fresh or dried in cooking. Capsaicin content gives chili peppers their characteristic heat.",
        isEdible = true
    ),
    "pineapple" to PlantData(
        englishName = "Pineapple",
        croatianName = "Ananas",
        latinName = "Ananas comosus",
        description = "A tropical plant with spiky leaves and a large, sweet, aromatic fruit. Contains bromelain, an enzyme with digestive properties.",
        isEdible = true
    ),
    "poppyanemone" to PlantData(
        englishName = "Poppy Anemone",
        croatianName = "Anemona",
        latinName = "Anemone coronaria",
        description = "A flowering plant producing vibrant red, white, or blue blooms. Popular in gardens and floral arrangements, not edible.",
        isEdible = false
    ),
    "pomelo" to PlantData(
        englishName = "Pomelo",
        croatianName = "Pomelo",
        latinName = "Citrus maxima",
        description = "The largest citrus fruit, with a thick rind and sweet to mildly tart pulp. Consumed fresh or in salads.",
        isEdible = true
    ),
    "portugueselaurel" to PlantData(
        englishName = "Portuguese Laurel",
        croatianName = "Portugalska lovorvišnja",
        latinName = "Prunus lusitanica",
        description = "An evergreen shrub or small tree with glossy leaves and fragrant white flowers. Commonly used as a hedge plant, parts are toxic if ingested.",
        isEdible = false
    ),
    "paddy" to PlantData(
        englishName = "Rice",
        croatianName = "Riža",
        latinName = "Oryza sativa",
        description = "A staple cereal crop cultivated in flooded fields, producing edible grains. Consumed worldwide in numerous cuisines.",
        isEdible = true
    ),
    "quincetree" to PlantData(
        englishName = "Quince Tree",
        croatianName = "Dunja",
        latinName = "Cydonia oblonga",
        description = "A deciduous tree bearing fragrant, yellow, pear-shaped fruits. Fruits are usually cooked or made into jams and jellies.",
        isEdible = true
    ),
    "reedgrass" to PlantData(
        englishName = "Reed Grass",
        croatianName = "Trska",
        latinName = "Phragmites australis",
        description = "A tall perennial grass common in wetlands. Provides habitat for wildlife but not typically consumed by humans.",
        isEdible = false
    ),
    "rockrose" to PlantData(
        englishName = "Rock Rose",
        croatianName = "Planinski bušin",
        latinName = "Cistus ladanifer",
        description = "A Mediterranean shrub with resinous, aromatic leaves and papery flowers. Known for producing labdanum resin used in perfumes.",
        isEdible = false
    ),
    "rosemary" to PlantData(
        englishName = "Rosemary",
        croatianName = "Ružmarin",
        latinName = "Salvia rosmarinus",
        description = "An aromatic evergreen shrub with needle-like leaves used in cooking and herbal medicine. Thrives in sunny, dry locations.",
        isEdible = true
    ),
    "rue" to PlantData(
        englishName = "Rue",
        croatianName = "Ruta",
        latinName = "Ruta graveolens",
        description = "A strongly aromatic herb with blue-green foliage, historically used in medicine and as a culinary herb in small amounts. Can be toxic in large doses.",
        isEdible = true
    ),
    "scarletpimpernel" to PlantData(
        englishName = "Scarlet Pimpernel",
        croatianName = "Crveni pimpernel",
        latinName = "Anagallis arvensis",
        description = "A low-growing plant with small scarlet flowers. Considered a weed in many regions and not edible.",
        isEdible = false
    ),
    "sealavender" to PlantData(
        englishName = "Sea Lavender",
        croatianName = "Morska lavanda",
        latinName = "Limonium vulgare",
        description = "A coastal perennial with clusters of small lavender-colored flowers. Grown for ornamental purposes, not edible.",
        isEdible = false
    ),
    "snakeroot" to PlantData(
        englishName = "Snakeroot",
        croatianName = "Zmijska trava",
        latinName = "Ageratina altissima",
        description = "A perennial herb with clusters of small white flowers. Some species are toxic and have historically caused livestock poisoning.",
        isEdible = false
    ),
    "snapdragon" to PlantData(
        englishName = "Snapdragon",
        croatianName = "Zmačac",
        latinName = "Antirrhinum majus",
        description = "An ornamental plant with tall spikes of colorful flowers that resemble a dragon’s mouth. Commonly used in gardens, not edible.",
        isEdible = false
    ),
    "snowdrop" to PlantData(
        englishName = "Snowdrop",
        croatianName = "Visibaba",
        latinName = "Galanthus nivalis",
        description = "A small bulbous plant that blooms in late winter with delicate white flowers. Poisonous if ingested.",
        isEdible = false
    ),
    "soybean" to PlantData(
        englishName = "Soybean",
        croatianName = "Soja",
        latinName = "Glycine max",
        description = "An annual legume producing protein-rich beans used for food, oil, and animal feed. Widely cultivated worldwide.",
        isEdible = true
    ),
    "spanishbroom" to PlantData(
        englishName = "Spanish Broom",
        croatianName = "Španjolski žuk",
        latinName = "Spartium junceum",
        description = "A perennial shrub with bright yellow flowers and slender green stems. Used ornamentally; toxic if ingested.",
        isEdible = false
    ),
    "spurge" to PlantData(
        englishName = "Spurge",
        croatianName = "Mliječ",
        latinName = "Euphorbia",
        description = "A large genus of plants with milky sap, often toxic. Includes both ornamental and weedy species.",
        isEdible = false
    ),
    "stonepine" to PlantData(
        englishName = "Stone Pine",
        croatianName = "Pinjol",
        latinName = "Pinus pinea",
        description = "An evergreen conifer native to the Mediterranean, producing edible pine nuts. Has an umbrella-shaped canopy.",
        isEdible = true
    ),
    "strawberrytree" to PlantData(
        englishName = "Strawberry Tree",
        croatianName = "Planika",
        latinName = "Arbutus unedo",
        description = "An evergreen shrub or small tree with red, rough-skinned edible fruits. Often used for jams and liqueurs.",
        isEdible = true
    ),
    "sweetchestnut" to PlantData(
        englishName = "Sweet Chestnut",
        croatianName = "Pitomi kesten",
        latinName = "Castanea sativa",
        description = "A large deciduous tree producing edible nuts enclosed in spiny husks. Valued for both timber and food.",
        isEdible = true
    ),
    "sweetpotato" to PlantData(
        englishName = "Sweet Potato",
        croatianName = "Batat",
        latinName = "Ipomoea batatas",
        description = "A perennial vine grown for its edible, sweet-tasting tubers. Leaves are also edible when cooked.",
        isEdible = true
    ),
    "tamarisk" to PlantData(
        englishName = "Tamarisk",
        croatianName = "Tamariš",
        latinName = "Tamarix",
        description = "A small tree or shrub with feathery branches and pink flowers. Used ornamentally and for erosion control, not edible.",
        isEdible = false
    ),
    "thyme" to PlantData(
        englishName = "Thyme",
        croatianName = "Timijan",
        latinName = "Thymus vulgaris",
        description = "A low-growing aromatic herb used for seasoning. Thrives in sunny, dry conditions and attracts pollinators.",
        isEdible = true
    ),
    "tobacco" to PlantData(
        englishName = "Tobacco",
        croatianName = "Duhan",
        latinName = "Nicotiana tabacum",
        description = "A tall annual plant cultivated for its leaves, which are processed for smoking and nicotine extraction. Poisonous if ingested raw.",
        isEdible = false
    ),
    "torreya" to PlantData(
        englishName = "Torreya",
        croatianName = "Toreja",
        latinName = "Torreya nucifera",
        description = "A slow-growing conifer with edible seeds in some species. Wood is valued in traditional crafts.",
        isEdible = true
    ),
    "treeHeath" to PlantData(
        englishName = "Tree Heath",
        croatianName = "Drvenasti vrijes",
        latinName = "Erica arborea",
        description = "An evergreen shrub or small tree with white or pink flowers. Known for its hard root wood used in making pipes.",
        isEdible = false
    ),
    "treemallow" to PlantData(
        englishName = "Tree Mallow",
        croatianName = "Drvenasti sljez",
        latinName = "Malva arborea",
        description = "A tall perennial plant with purple-pink flowers. Leaves and young shoots are edible when cooked.",
        isEdible = true
    ),
    "trumpetcreeper" to PlantData(
        englishName = "Trumpet Creeper",
        croatianName = "Trubasta lozica",
        latinName = "Campsis radicans",
        description = "A vigorous climbing plant with large trumpet-shaped orange-red flowers. Used ornamentally, not edible.",
        isEdible = false
    ),
    "valerian" to PlantData(
        englishName = "Valerian",
        croatianName = "Valerijana",
        latinName = "Valeriana officinalis",
        description = "A perennial plant with clusters of small fragrant flowers. Roots are used medicinally as a mild sedative.",
        isEdible = true
    ),
    "wallflower" to PlantData(
        englishName = "Wallflower",
        croatianName = "Zidni cvijet",
        latinName = "Erysimum cheiri",
        description = "A fragrant biennial or perennial with yellow to orange flowers. Grown ornamentally, not edible.",
        isEdible = false
    ),
    "watermint" to PlantData(
        englishName = "Water Mint",
        croatianName = "Vodena metvica",
        latinName = "Mentha aquatica",
        description = "A perennial herb found in damp habitats, with aromatic leaves used for teas and flavoring.",
        isEdible = true
    ),
    "watermelon" to PlantData(
        englishName = "Watermelon",
        croatianName = "Lubenica",
        latinName = "Citrullus lanatus",
        description = "A sprawling annual plant producing large, juicy fruits with sweet red or yellow flesh. Popular in summer.",
        isEdible = true
    ),
    "whiteasphodel" to PlantData(
        englishName = "White Asphodel",
        croatianName = "Bijeli asfodel",
        latinName = "Asphodelus albus",
        description = "A perennial with tall flower spikes of star-shaped white blooms. Roots were historically eaten but are not common today.",
        isEdible = true
    ),
    "wildcarrot" to PlantData(
        englishName = "Wild Carrot",
        croatianName = "Divlja mrkva",
        latinName = "Daucus carota",
        description = "A biennial plant with finely divided leaves and white umbel flowers. Root is edible when young but becomes woody with age.",
        isEdible = true
    ),
    "rose" to PlantData(
        englishName = "Rose",
        croatianName = "Ruža",
        latinName = "Rosa",
        description = "A woody perennial flowering plant with fragrant blooms in many colors. Petals are edible and often used in culinary products.",
        isEdible = true
    ),
    "wintersavory" to PlantData(
        englishName = "Winter Savory",
        croatianName = "Vrisak",
        latinName = "Satureja montana",
        description = "A perennial herb with a peppery flavor, used for seasoning meats and beans. Thrives in sunny, dry soils.",
        isEdible = true
    ),
    "wisteria" to PlantData(
        englishName = "Wisteria",
        croatianName = "Glicinija",
        latinName = "Wisteria sinensis",
        description = "A climbing vine with cascading clusters of purple or white flowers. All parts except flowers are toxic.",
        isEdible = false
    ),
    "yewtree" to PlantData(
        englishName = "Yew Tree",
        croatianName = "Tisa",
        latinName = "Taxus baccata",
        description = "An evergreen conifer with red berry-like arils. All parts except the fleshy aril are highly toxic.",
        isEdible = false
    ),
    "yucca" to PlantData(
        englishName = "Yucca",
        croatianName = "Juka",
        latinName = "Yucca filamentosa",
        description = "A perennial shrub with sword-like leaves and tall spikes of white flowers. Flowers and fruits are edible in some species.",
        isEdible = true
    ),
    "ziziphus" to PlantData(
        englishName = "Jujube",
        croatianName = "Cikla",
        latinName = "Ziziphus jujuba",
        description = "A small deciduous tree producing sweet, date-like fruits. Eaten fresh, dried, or used in herbal remedies.",
        isEdible = true
    ),
    "zucchiniplant" to PlantData(
        englishName = "Zucchini",
        croatianName = "Tikvica",
        latinName = "Cucurbita pepo",
        description = "A summer squash with tender edible fruits harvested when immature. Versatile in cooking and easy to grow.",
        isEdible = true
    )
)