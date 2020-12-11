package main.java.util.definition.cache;

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
import main.java.util.sound.Sound;

public final class Caches {
    public static enum Affixes {
        PLANT_PREFIXES,
        PLANT_SUFFIXES,
        FERTILIZE_PREFIXES,
        FERTILIZE_SUFFIXES,
        PESTICIDE_PREFIXES,
        PESTICIDE_SUFFIXES,
        WORKER_PREFIXS,
        WORKER_NAMES;
        private final DefCache<Affix> cache = new DefCache<>();

        public DefCache<Affix> instance() {
            return cache;
        }
    };
    public enum SingleNames {
        FIRST,
        LAST;
        private final DefCache<Name> cache = new DefCache<>();

        public DefCache<Name> instance() {
            return cache;
        }
    }
    public static final DefCache<Sound> SOUNDS = new DefCache<>();
    public static final DefCache<Difficulty> DIFFICULTIES = new DefCache<>();
    public static final DefCache<Market> MARKETS = new DefCache<>();
    public static final DefCache<PlantDef> PLANT_DEFS = new DefCache<>();
    public static final DefCache<WorkerDef> WORKER_DEFS = new DefCache<>();
    public static final DefCache<PesticideDef> PESTICIDE_DEFS = new DefCache<>();
    public static final DefCache<FertilizerDef> FERTILIZER_DEFS = new DefCache<>();
    public static final DefCache<IrrigationDef> IRRIGATION_DEFS = new DefCache<>();
    public static final DefCache<TractorDef> TRACTOR_DEFS = new DefCache<>();
}
