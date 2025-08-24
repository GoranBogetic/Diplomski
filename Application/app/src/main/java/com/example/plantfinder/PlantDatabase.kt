package com.example.plantfinder

data class PlantData(
    val englishName: String,
    val croatianName: String,
    val latinName: String,
    val description: String,
    val isEdible: Boolean
)

val plantInfo = mapOf(
    "agaveamericana" to PlantData(
        englishName = "Agave Americana",
        croatianName = "Američka agava",
        latinName = "Agave americana",
        description = "Large, evergreen succulent forming a rosette of thick, spiny leaves; monocarpic. Cultivated for ornament, fiber and sweet sap; flowers and stalk traditionally roasted.",
        isEdible = true
    ),
    "allepopine" to PlantData(
        englishName = "Aleppo Pine",
        croatianName = "Alepski bor",
        latinName = "Pinus halepensis",
        description = "Mediterranean pine with long needles and irregular crown; used for reforestation and as an ornamental. Seeds small; tree primarily non-food.",
        isEdible = false
    ),
    "almond" to PlantData(
        englishName = "Almond",
        croatianName = "Badem",
        latinName = "Prunus dulcis",
        description = "Small tree cultivated for edible nuts (sweet cultivars); blossoms early in spring and prefers warm, dry climates.",
        isEdible = true
    ),
    "alpinerose" to PlantData(
        englishName = "Alpine Rose",
        croatianName = "Alpska ruža (rododendron)",
        latinName = "Rhododendron ferrugineum",
        description = "Evergreen alpine shrub with pink bell-shaped flowers; leaves contain grayanotoxins and are poisonous if ingested.",
        isEdible = false
    ),
    "applemint" to PlantData(
        englishName = "Apple Mint",
        croatianName = "Jabučna metvica",
        latinName = "Mentha suaveolens",
        description = "Aromatic mint with softly hairy leaves and a sweet, fruity scent; grown for teas, desserts and garnishes.",
        isEdible = true
    ),
    "beautifulflax" to PlantData(
        englishName = "Beautiful Flax",
        croatianName = "Lijepi lan",
        latinName = "Linum narbonense",
        description = "Perennial flax with showy blue flowers; ornamental species related to cultivated flax; not typically used as food.",
        isEdible = false
    ),
    "blackbogrush" to PlantData(
        englishName = "Black Bog-rush",
        croatianName = "Crni šaš",
        latinName = "Schoenus nigricans",
        description = "Clump-forming sedge of fens and alkaline marshes; dark inflorescences; ecological indicator, not used as food.",
        isEdible = false
    ),
    "bristelberry" to PlantData(
        englishName = "Bristly Blackberry",
        croatianName = "Kupina bodljikava",
        latinName = "Rubus setosus",
        description = "Thorny bramble with edible aggregate fruits; identification within the Rubus genus is complex and regional.",
        isEdible = true
    ),
    "buckshornplantain" to PlantData(
        englishName = "Buck's-horn Plantain",
        croatianName = "Rogati trputac",
        latinName = "Plantago coronopus",
        description = "Low rosette herb of coasts and sandy soils; deeply lobed leaves eaten young in salads; mildly salty taste.",
        isEdible = true
    ),
    "cantabricanmorningglory" to PlantData(
        englishName = "Cantabrican Morning Glory",
        croatianName = "Kantabrijski slak",
        latinName = "Convolvulus cantabrica",
        description = "Low, silvery perennial bindweed with funnel flowers; mostly ornamental/wild; not used as food.",
        isEdible = false
    ),
    "carob" to PlantData(
        englishName = "Carob",
        croatianName = "Rogač",
        latinName = "Ceratonia siliqua",
        description = "Evergreen Mediterranean tree; pods ground into sweet flour used as cocoa substitute; seeds historically used as carats.",
        isEdible = true
    ),
    "catnip" to PlantData(
        englishName = "Catnip",
        croatianName = "Mačja metvica",
        latinName = "Nepeta cataria",
        description = "Mint family herb valued for calming teas and for its effect on cats; leaves and flowers aromatic and usable fresh or dried.",
        isEdible = true
    ),
    "cherrylaurel" to PlantData(
        englishName = "Cherry Laurel",
        croatianName = "Lovorvišnja",
        latinName = "Prunus laurocerasus",
        description = "Evergreen hedge shrub with glossy leaves; contains cyanogenic compounds; fruit and foliage considered poisonous.",
        isEdible = false
    ),
    "chimneybellflower" to PlantData(
        englishName = "Chimney Bellflower",
        croatianName = "Piramidalna zvončika",
        latinName = "Campanula pyramidalis",
        description = "Tall biennial/perennial with pyramid-shaped clusters of blue bellflowers; many Campanula species have edible young leaves/flowers.",
        isEdible = true
    ),
    "commonbuckthorn" to PlantData(
        englishName = "Common Buckthorn",
        croatianName = "Obična krkavina",
        latinName = "Rhamnus cathartica",
        description = "Spiny shrub with purgative, poisonous berries; invasive in many regions; not for consumption.",
        isEdible = false
    ),
    "commoncentaury" to PlantData(
        englishName = "Common Centaury",
        croatianName = "Obična kičica",
        latinName = "Centaurium erythraea",
        description = "Small biennial with pink starry flowers; intensely bitter medicinal herb used for digestive bitters rather than food.",
        isEdible = false
    ),
    "commonhazel" to PlantData(
        englishName = "Common Hazel",
        croatianName = "Lijeska",
        latinName = "Corylus avellana",
        description = "Multi-stemmed shrub/tree producing edible hazelnuts; important wildlife and agroforestry species.",
        isEdible = true
    ),
    "commonjuniper" to PlantData(
        englishName = "Common Juniper",
        croatianName = "Kleka",
        latinName = "Juniperus communis",
        description = "Coniferous shrub/tree with berry-like cones used to flavor dishes and gin; resinous, aromatic taste.",
        isEdible = true
    ),
    "commonlavender" to PlantData(
        englishName = "Common Lavender",
        croatianName = "Lavanda",
        latinName = "Lavandula angustifolia",
        description = "Fragrant subshrub with narrow leaves and purple flower spikes; culinary lavender used sparingly in sweets and teas.",
        isEdible = true
    ),
    "commonorache" to PlantData(
        englishName = "Common Orache",
        croatianName = "Obična loboda",
        latinName = "Atriplex patula",
        description = "Goosefoot relative with arrow-shaped leaves; young leaves eaten like spinach; tolerates saline soils.",
        isEdible = true
    ),
    "commonprivet" to PlantData(
        englishName = "Common Privet",
        croatianName = "Kalina (ligustrum)",
        latinName = "Ligustrum vulgare",
        description = "Deciduous to semi-evergreen hedge shrub with black berries; fruits and leaves are poisonous to humans.",
        isEdible = false
    ),
    "commonsealavender" to PlantData(
        englishName = "Common Sea Lavender",
        croatianName = "Morska lavanda",
        latinName = "Limonium vulgare",
        description = "Saltmarsh perennial with papery lavender-colored flowers; ornamental and for dried arrangements; not used as food.",
        isEdible = false
    ),
    "corneliancherry" to PlantData(
        englishName = "Cornelian Cherry",
        croatianName = "Dren",
        latinName = "Cornus mas",
        description = "Large shrub/small tree with early yellow flowers and tart red drupes used for syrups, jams and liqueurs.",
        isEdible = true
    ),
    "dogrose" to PlantData(
        englishName = "Dog Rose",
        croatianName = "Pasja ruža (šipak)",
        latinName = "Rosa canina",
        description = "Climbing wild rose with hooked prickles; vitamin C–rich hips used for teas, syrups and jams (remove irritating hairs).",
        isEdible = true
    ),
    "dalmationpyrethrum" to PlantData(
        englishName = "Dalmatian Pyrethrum",
        croatianName = "Dalmatinski buhač",
        latinName = "Tanacetum cinerariifolium",
        description = "Daisy producing natural insecticidal pyrethrins; used as botanical pesticide; not for culinary use.",
        isEdible = false
    ),
    "englishoak" to PlantData(
        englishName = "English Oak",
        croatianName = "Hrast lužnjak",
        latinName = "Quercus robur",
        description = "Large deciduous oak; acorns high in tannins but edible after leaching/processing; important timber tree.",
        isEdible = true
    ),
    "europeanbarberry" to PlantData(
        englishName = "European Barberry",
        croatianName = "Obična žutika",
        latinName = "Berberis vulgaris",
        description = "Spiny shrub with sour red berries used in jams and Middle Eastern cuisine; leaves and bark are bitter/medicinal.",
        isEdible = true
    ),
    "europeanbeech" to PlantData(
        englishName = "European Beech",
        croatianName = "Obična bukva",
        latinName = "Fagus sylvatica",
        description = "Majestic deciduous tree; triangular beechnuts edible roasted in small amounts; raw nuts mildly toxic.",
        isEdible = true
    ),
    "europeanblackelderberry" to PlantData(
        englishName = "European Black Elderberry",
        croatianName = "Crna bazga",
        latinName = "Sambucus nigra",
        description = "Shrub with umbels of white flowers and black berries; flowers and cooked ripe berries used for syrups and desserts.",
        isEdible = true
    ),
    "europeanholly" to PlantData(
        englishName = "European Holly",
        croatianName = "Božikovina",
        latinName = "Ilex aquifolium",
        description = "Evergreen with spiny leaves and red berries; berries are poisonous and not for consumption.",
        isEdible = false
    ),
    "europeanhornbeam" to PlantData(
        englishName = "European Hornbeam",
        croatianName = "Obični grab",
        latinName = "Carpinus betulus",
        description = "Hardwood tree of mixed forests; valued for tough timber; not used as a food plant.",
        isEdible = false
    ),
    "europeanmountainash" to PlantData(
        englishName = "European Mountain Ash",
        croatianName = "Jarebika",
        latinName = "Sorbus aucuparia",
        description = "Small tree with clusters of orange-red berries; very tart/astringent raw; edible after frost/cooking (jellies, wines).",
        isEdible = true
    ),
    "fig" to PlantData(
        englishName = "Fig",
        croatianName = "Smokva",
        latinName = "Ficus carica",
        description = "Mediterranean fruit tree bearing sweet, soft figs; both fresh and dried are widely consumed.",
        isEdible = true
    ),
    "fleshyrussianthistle" to PlantData(
        englishName = "Fleshy Russian Thistle",
        croatianName = "Sočna solnjača",
        latinName = "Salsola soda",
        description = "Halophytic annual (agretti) cultivated as a vegetable in Italy; tender shoots eaten sautéed or blanched.",
        isEdible = true
    ),
    "gardensage" to PlantData(
        englishName = "Garden Sage",
        croatianName = "Kadulja",
        latinName = "Salvia officinalis",
        description = "Woody perennial herb with gray leaves; classic culinary seasoning and medicinal plant native to the Adriatic coasts.",
        isEdible = true
    ),
    "goatwillow" to PlantData(
        englishName = "Goat Willow",
        croatianName = "Iva (vrba iva)",
        latinName = "Salix caprea",
        description = "Early-flowering willow with catkins; historically medicinal (salicin); not a regular food plant.",
        isEdible = false
    ),
    "goldensamphire" to PlantData(
        englishName = "Golden Samphire",
        croatianName = "Zlatna obalarka",
        latinName = "Limbarda crithmoides",
        description = "Coastal perennial with fleshy leaves historically pickled or eaten as a seaside vegetable; aromatic taste.",
        isEdible = true
    ),
    "greatstingingnettle" to PlantData(
        englishName = "Great Stinging Nettle",
        croatianName = "Velika kopriva",
        latinName = "Urtica dioica",
        description = "Perennial with stinging hairs; young shoots and leaves edible when cooked; used in soups, pies and teas.",
        isEdible = true
    ),
    "holmoak" to PlantData(
        englishName = "Holm Oak",
        croatianName = "Crnika",
        latinName = "Quercus ilex",
        description = "Evergreen Mediterranean oak; sweet varieties of acorns traditionally eaten or fed to livestock after processing.",
        isEdible = true
    ),
    "indianfigopuntia" to PlantData(
        englishName = "Indian Fig Opuntia",
        croatianName = "Indijska smokva (opuncija)",
        latinName = "Opuntia ficus-indica",
        description = "Cactus cultivated for edible fruits (tunas) and pads (nopales); drought tolerant and widely naturalized.",
        isEdible = true
    ),
    "iris" to PlantData(
        englishName = "Iris",
        croatianName = "Perunika (iris)",
        latinName = "Iris germanica",
        description = "Showy rhizomatous perennial grown for flowers; many Iris species are toxic and not used as food.",
        isEdible = false
    ),
    "italianarum" to PlantData(
        englishName = "Italian Arum",
        croatianName = "Talijanski kozlac",
        latinName = "Arum italicum",
        description = "Shade-loving aroid with arrow leaves and red berries; contains calcium oxalate crystals; poisonous if ingested.",
        isEdible = false
    ),
    "jerisalemlampwick" to PlantData(
        englishName = "Jerusalem Lampwick (Jerusalem Sage)",
        croatianName = "Jeruzalemska kadulja",
        latinName = "Phlomis fruticosa",
        description = "Woolly-leaved Mediterranean shrub with yellow whorled flowers; aromatic ornamental; not a food plant.",
        isEdible = false
    ),
    "jerusalemthorn" to PlantData(
        englishName = "Jerusalem Thorn",
        croatianName = "Jeruzalemski trn",
        latinName = "Parkinsonia aculeata",
        description = "Spiny, drought-tolerant legume tree with green photosynthetic twigs; grown ornamentally; not used as food.",
        isEdible = false
    ),
    "jointpine" to PlantData(
        englishName = "Joint-pine",
        croatianName = "Efedra",
        latinName = "Ephedra fragilis",
        description = "Shrubby gymnosperm of dry coasts; jointed green stems; historically medicinal (ephedrine); not a food plant.",
        isEdible = false
    ),
    "lilacchastetree" to PlantData(
        englishName = "Lilac Chaste Tree",
        croatianName = "Fratarski papar",
        latinName = "Vitex agnus-castus",
        description = "Aromatic shrub of shores and streams; peppery seeds used as spice substitute; leaves/flowers aromatic.",
        isEdible = true
    ),
    "longbractedsedge" to PlantData(
        englishName = "Long-bracted Sedge",
        croatianName = "Dugoklasasti šaš",
        latinName = "Carex distachya",
        description = "Tufted sedge of dry, sandy places; valued ecologically; not used as food.",
        isEdible = false
    ),
    "mastic" to PlantData(
        englishName = "Mastic",
        croatianName = "Smrdljika (mastika)",
        latinName = "Pistacia lentiscus",
        description = "Evergreen shrub/tree producing aromatic resin (mastic) chewed and used to flavor sweets and liqueurs.",
        isEdible = true
    ),
    "mediterraneancypress" to PlantData(
        englishName = "Mediterranean Cypress",
        croatianName = "Čempres",
        latinName = "Cupressus sempervirens",
        description = "Tall, columnar conifer iconic of Mediterranean landscapes; ornamental and timber; not a food plant.",
        isEdible = false
    ),
    "mezeron" to PlantData(
        englishName = "Mezereon",
        croatianName = "Likovac",
        latinName = "Daphne mezereum",
        description = "Fragrant early-flowering shrub with red berries; extremely poisonous; never eat.",
        isEdible = false
    ),
    "myrtle" to PlantData(
        englishName = "Myrtle",
        croatianName = "Mirta",
        latinName = "Myrtus communis",
        description = "Evergreen shrub with aromatic leaves and blue-black berries; used to flavor meats and liqueurs (mirto).",
        isEdible = true
    ),
    "olive" to PlantData(
        englishName = "Olive",
        croatianName = "Maslina",
        latinName = "Olea europaea",
        description = "Long-lived evergreen tree cultivated for edible fruits (after curing) and oil; hallmark of Mediterranean agriculture.",
        isEdible = true
    ),
    "pomegranate" to PlantData(
        englishName = "Pomegranate",
        croatianName = "Nar (šipak)",
        latinName = "Punica granatum",
        description = "Deciduous shrub/tree with leathery-skinned fruits full of juicy arils; eaten fresh and juiced.",
        isEdible = true
    ),
    "redraspberry" to PlantData(
        englishName = "Red Raspberry",
        croatianName = "Malina",
        latinName = "Rubus idaeus",
        description = "Perennial cane fruit producing aromatic red berries; widely cultivated and foraged in cooler regions.",
        isEdible = true
    ),
    "savinjuniper" to PlantData(
        englishName = "Savin Juniper",
        croatianName = "Sabina kleka",
        latinName = "Juniperus sabina",
        description = "Low, spreading juniper; foliage and berries are toxic (savin oil); not for culinary use.",
        isEdible = false
    ),
    "seabeet" to PlantData(
        englishName = "Sea Beet",
        croatianName = "Obalna blitva",
        latinName = "Beta vulgaris subsp. maritima",
        description = "Wild ancestor of beet and chard; young leaves edible like spinach; thrives in coastal habitats.",
        isEdible = true
    ),
    "seaclubrush" to PlantData(
        englishName = "Sea Clubrush",
        croatianName = "Morska štapika",
        latinName = "Bolboschoenus maritimus",
        description = "Coastal sedge with underground tubers; tubers have been eaten historically in some regions.",
        isEdible = true
    ),
    "seafig" to PlantData(
        englishName = "Sea Fig (Hottentot Fig)",
        croatianName = "Mesemb (smokva morska)",
        latinName = "Carpobrotus edulis",
        description = "Prostrate succulent forming mats on coasts; edible tart fruits and fleshy leaves (pickled/cooked).",
        isEdible = true
    ),
    "smokebush" to PlantData(
        englishName = "Smokebush",
        croatianName = "Rujevina",
        latinName = "Cotinus coggygria",
        description = "Shrub/tree with plume-like inflorescences that look like smoke; leaves used for dye; not a food plant.",
        isEdible = false
    ),
    "spanishbroom" to PlantData(
        englishName = "Spanish Broom",
        croatianName = "Španjolski žuk",
        latinName = "Spartium junceum",
        description = "Leafless green stems with fragrant yellow flowers; contains toxic alkaloids; not edible.",
        isEdible = false
    ),
    "spindle" to PlantData(
        englishName = "Spindle",
        croatianName = "Kurika (paklen)",
        latinName = "Euonymus europaeus",
        description = "Shrub with pink/orange capsules; attractive but poisonous fruits and seeds; historically used for spindles.",
        isEdible = false
    ),
    "spinyasparagus" to PlantData(
        englishName = "Spiny Asparagus",
        croatianName = "Divlja šparoga (oštra)",
        latinName = "Asparagus acutifolius",
        description = "Dioecious, spiny Mediterranean asparagus; young spring shoots harvested as a prized wild vegetable.",
        isEdible = true
    ),
    "spinyrestharrow" to PlantData(
        englishName = "Spiny Restharrow",
        croatianName = "Bodljikava roljika",
        latinName = "Ononis spinosa",
        description = "Spiny pink-flowered legume of dry grasslands; roots used medicinally as diuretic; not a food plant.",
        isEdible = false
    ),
    "spinystarwort" to PlantData(
        englishName = "Spiny Starwort",
        croatianName = "Bodljikavi zvjezdan",
        latinName = "Pallenis spinosa",
        description = "Drought-tolerant annual with spiny bracts and yellow daisies; ornamental/wild; not edible.",
        isEdible = false
    ),
    "springadonis" to PlantData(
        englishName = "Spring Adonis",
        croatianName = "Proljetni ranjenik",
        latinName = "Adonis vernalis",
        description = "Showy yellow-flowered steppe perennial; cardiac glycosides present; poisonous and not edible.",
        isEdible = false
    ),
    "stonepine" to PlantData(
        englishName = "Stone Pine",
        croatianName = "Pinj (pinija)",
        latinName = "Pinus pinea",
        description = "Umbrella-shaped Mediterranean pine producing large edible pine nuts; valued ornamental and for nuts.",
        isEdible = true
    ),
    "strawberrytree" to PlantData(
        englishName = "Strawberry Tree",
        croatianName = "Planika (maginja)",
        latinName = "Arbutus unedo",
        description = "Evergreen with red, rough-skinned fruits ripening in autumn; fruits edible fresh or for jams and liqueurs.",
        isEdible = true
    ),
    "sweatchestnut" to PlantData(
        englishName = "Sweet Chestnut",
        croatianName = "Pitomi kesten",
        latinName = "Castanea sativa",
        description = "Large chestnut valued for edible nuts (roasted, candied, flour) and durable timber; prefers acidic soils.",
        isEdible = true
    ),
    "terebinth" to PlantData(
        englishName = "Terebinth",
        croatianName = "Terebint (crna tršlja)",
        latinName = "Pistacia terebinthus",
        description = "Deciduous Pistacia with resinous aroma; young shoots and galls used traditionally; edible uses regional.",
        isEdible = true
    ),
    "trailingbellflower" to PlantData(
        englishName = "Trailing Bellflower",
        croatianName = "Viseća zvončika",
        latinName = "Campanula poscharskyana",
        description = "Trailing alpine bellflower used as groundcover; young leaves and flowers mild and edible.",
        isEdible = true
    ),
    "treeheath" to PlantData(
        englishName = "Tree Heath",
        croatianName = "Bijeli vrijes",
        latinName = "Erica arborea",
        description = "Evergreen shrub/tree with tiny leaves and white bells; used for briar pipes (root burl); not a food plant.",
        isEdible = false
    ),
    "turkeyoak" to PlantData(
        englishName = "Turkey Oak",
        croatianName = "Cerić (medunac istočni)",
        latinName = "Quercus cerris",
        description = "Fast-growing oak with bristly acorn cups; acorns high in tannins; edible only after thorough processing.",
        isEdible = true
    ),
    "wayfaringtree" to PlantData(
        englishName = "Wayfaring Tree",
        croatianName = "Glogovac (kalina krupnolisna)",
        latinName = "Viburnum lantana",
        description = "Shrub with felted leaves and red-to-black berries; fruits are mildly toxic/raw and not used as food.",
        isEdible = false
    ),
    "yellowonion" to PlantData(
        englishName = "Yellow Onion",
        croatianName = "Luk",
        latinName = "Allium cepa",
        description = "Bulb onion cultivated worldwide; pungent when raw, sweet when caramelized; staple culinary vegetable.",
        isEdible = true
    )
)
