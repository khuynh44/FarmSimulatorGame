package main.java.util.definition;

import java.io.InputStream;
import java.util.List;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.BeanAccess;

import main.java.entities.Affix;
import main.java.entities.Difficulty;
import main.java.entities.Name;
import main.java.entities.items.definitions.FertilizerDef;
import main.java.entities.items.definitions.IrrigationDef;
import main.java.entities.items.definitions.PesticideDef;
import main.java.entities.items.definitions.PlantDef;
import main.java.entities.items.definitions.TractorDef;
import main.java.entities.items.definitions.WorkerDef;
import main.java.ui.market.Market;
import main.java.util.Constants;
import main.java.util.definition.cache.Caches;
import main.java.util.sound.Sound;

public final class Definitions {
    private Definitions() {
    }

    private static void load(String yamlFileName, Class<?> definitionClass,
            ILoadOperation loadOperation) {
        final Constructor constructor = new Constructor(definitionClass);
        constructor.setAllowDuplicateKeys(true);

        final Yaml yaml = new Yaml(constructor);
        yaml.setBeanAccess(BeanAccess.FIELD);

        InputStream inputStream =
            Definitions.class.getResourceAsStream(Constants.PATH_TO_DEFINITIONS + yamlFileName);

        if (inputStream == null) {
            System.out.println("--- Unable to load " + yamlFileName + " definitions. ---");
            return;
        }

        Iterable<Object> loadedDefs = yaml.loadAll(inputStream);
        loadedDefs.forEach(def -> loadOperation.apply(def));
    }

    public static void loadPlants() {
        System.out.println("loading items: ");
        load("plants.yaml", PlantDef.class, (def) -> {
            PlantDef plantDef = (PlantDef) def;
            System.out.println("  - " + plantDef.getId());
            plantDef.loadImages();
            Caches.PLANT_DEFS.intern(plantDef);
        });
    }

    public static void loadPesticide() {
        System.out.println("loading pesticides: ");
        load("pesticides.yaml", PesticideDef.class, (def) -> {
            PesticideDef pesticideDef = (PesticideDef) def;
            System.out.println("  - " + pesticideDef.getId());
            pesticideDef.loadImages();
            Caches.PESTICIDE_DEFS.intern(pesticideDef);
        });
    }

    public static void loadIrrigation() {
        System.out.println("loading irrigation: ");
        load("irrigation.yaml", IrrigationDef.class, (def) -> {
            IrrigationDef irrigationDef = (IrrigationDef) def;
            System.out.println("  - " + irrigationDef.getId());
            irrigationDef.loadImages();
            Caches.IRRIGATION_DEFS.intern(irrigationDef);
        });
    }

    public static void loadTractor() {
        System.out.println("loading tractor: ");
        load("tractor.yaml", TractorDef.class, (def) -> {
            TractorDef tractorDef = (TractorDef) def;
            System.out.println("  - " + tractorDef.getId());
            tractorDef.loadImages();
            Caches.TRACTOR_DEFS.intern(tractorDef);
        });
    }

    public static void loadMarkets() {
        System.out.println("loading markets: ");
        load("markets.yaml", Market.class, (def) -> {
            Market market = (Market) def;
            System.out.println("  - " + market.getId());
            market.onLoad();
            Caches.MARKETS.intern(market);
        });
    }

    public static void loadSounds() {
        System.out.println("loading sounds: ");
        load("sounds.yaml", Sound.class, (def) -> {
            Sound sound = (Sound) def;
            System.out.println("  - " + sound.getId());
            sound.loadSound();
            Caches.SOUNDS.intern(sound);
        });
    }

    public static void loadAffixes() {
        loadPlantPrefixes();
        loadPlantSuffixes();

        loadFertilizerPrefixes();
        loadFertilizerSuffixes();

        loadPesticidePrefixes();
        loadPesticideSuffixes();
    }

    private static void loadPlantPrefixes() {
        System.out.println("loading plant prefixes: ");
        load("plant_prefixes.yaml", Affix.class, (def) -> {
            Affix affix = (Affix) def;
            System.out.println("  - " + affix);
            Caches.Affixes.PLANT_PREFIXES.instance().intern(affix);
        });
    }

    private static void loadPlantSuffixes() {
        System.out.println("loading plant suffixes: ");
        load("plant_suffixes.yaml", Affix.class, (def) -> {
            Affix affix = (Affix) def;
            System.out.println("  - " + affix);
            Caches.Affixes.PLANT_SUFFIXES.instance().intern(affix);
        });
    }

    private static void loadFertilizerPrefixes() {
        System.out.println("loading fertilizer prefixes: ");
        load("fertilizer_prefixes.yaml", Affix.class, (def) -> {
            Affix affix = (Affix) def;
            System.out.println("  - " + affix);
            Caches.Affixes.FERTILIZE_PREFIXES.instance().intern(affix);
        });
    }

    private static void loadFertilizerSuffixes() {
        System.out.println("loading fertilizer suffixes: ");
        load("fertilizer_suffixes.yaml", Affix.class, (def) -> {
            Affix affix = (Affix) def;
            System.out.println("  - " + affix);
            Caches.Affixes.FERTILIZE_SUFFIXES.instance().intern(affix);
        });
    }

    private static void loadPesticidePrefixes() {
        System.out.println("loading pesticide prefixes: ");
        load("pesticide_prefixes.yaml", Affix.class, (def) -> {
            Affix affix = (Affix) def;
            System.out.println("  - " + affix);
            Caches.Affixes.PESTICIDE_PREFIXES.instance().intern(affix);
        });
    }

    private static void loadPesticideSuffixes() {
        System.out.println("loading pesticide suffixes: ");
        load("pesticide_suffixes.yaml", Affix.class, (def) -> {
            Affix affix = (Affix) def;
            System.out.println("  - " + affix);
            Caches.Affixes.PESTICIDE_SUFFIXES.instance().intern(affix);
        });
    }

    public static void loadWorkerNames() {
        System.out.println("loading worker names: ");
        load("worker_names.yaml", NameWrapper.class, (def) -> {
            NameWrapper nameWrapper = (NameWrapper) def;
            System.out.println("  - first names: ");
            for (String name : nameWrapper.firstNames) {
                System.out.println("     - " + name);
                Caches.SingleNames.FIRST.instance().intern(new Name(name));
            }

            System.out.println("  - last names: ");
            for (String name : nameWrapper.lastNames) {
                System.out.println("     - " + name);
                Caches.SingleNames.LAST.instance().intern(new Name(name));
            }
        });
    }

    public static void loadWorkers() {
        System.out.println("loading workers: ");
        load("workers.yaml", WorkerDef.class, (def) -> {
            WorkerDef workerDef = (WorkerDef) def;
            System.out.println("  - " + workerDef.getId());
            workerDef.loadImages();
            Caches.WORKER_DEFS.intern(workerDef);
        });
    }

    public static void loadDifficulties() {
        System.out.println("loading difficulties: ");
        load("difficulties.yaml", Difficulty.class, (def) -> {
            Difficulty difficulty = (Difficulty) def;
            System.out.println("  - " + difficulty.getId());
            Caches.DIFFICULTIES.intern(difficulty);
        });
    }

    public static void loadfertilizers() {
        System.out.println("loading fertilizers: ");
        load("fertilizer.yaml", FertilizerDef.class, (def) -> {
            FertilizerDef fertilizerDef = (FertilizerDef) def;
            System.out.println("  - " + fertilizerDef.getId());
            fertilizerDef.loadImages();
            fertilizerDef.loadColor();
            Caches.FERTILIZER_DEFS.intern(fertilizerDef);
        });
    }

    private static class NameWrapper {
        private List<String> firstNames;
        private List<String> lastNames;
    }

    @FunctionalInterface
    private static interface ILoadOperation {
        void apply(Object def);
    }
}
