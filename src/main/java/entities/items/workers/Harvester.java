package main.java.entities.items.workers;

import main.java.entities.Affixes;
import main.java.entities.Player;
import main.java.entities.items.Plant;
import main.java.entities.items.definitions.WorkerDef;
import main.java.ui.farm.Plot;
import main.java.ui.inventory.Inventory;
import main.java.ui.inventory.InventoryModel;
import main.java.ui.market.Market;
import main.java.util.definition.cache.Caches;

public class Harvester extends Worker {
    private static final Market HARVESTER_MARKET =
        new Market("harvesters_market", "Harvesters Market", 0.10, Inventory.INF_CAP);
    private static final InventoryModel INV = new InventoryModel(Inventory.INF_CAP);

    public Harvester(int activeImageIndex, Affixes affixes) {
        super(Caches.WORKER_DEFS.get("harvester"), activeImageIndex, affixes);
    }

    public Harvester(WorkerDef def) {
        super(def);
    }

    public Harvester() {
        this(Caches.WORKER_DEFS.get("harvester"));
    }

    @Override
    public void work(Plot plot) {
        final Plant plant = plot.harvest();
        INV.incrementCount(plant);
        HARVESTER_MARKET.sellItem(Player.getInstance().getWallet(), plant, INV);
    }
}
