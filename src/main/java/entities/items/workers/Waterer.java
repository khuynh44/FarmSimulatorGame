package main.java.entities.items.workers;

import main.java.entities.Affixes;
import main.java.entities.items.definitions.WorkerDef;
import main.java.ui.farm.Plot;
import main.java.util.definition.cache.Caches;

public class Waterer extends Worker {
    public Waterer(WorkerDef def) {
        super(def);
    }

    public Waterer() {
        this(Caches.WORKER_DEFS.get("waterer"));
    }

    public Waterer(int activeImageIndex, Affixes affixes) {
        super(Caches.WORKER_DEFS.get("waterer"), activeImageIndex, affixes);
    }

    @Override
    public void work(Plot plot) {
        plot.water();
    }
}
