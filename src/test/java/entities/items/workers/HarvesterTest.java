package test.java.entities.items.workers;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.java.entities.Player;
import main.java.entities.items.Plant;
import main.java.entities.items.definitions.PlantDef;
import main.java.entities.items.definitions.WorkerDef;
import main.java.entities.items.workers.Harvester;
import main.java.entities.items.workers.Worker;
import main.java.ui.farm.Plot;
import main.java.util.definition.cache.Caches;

public class HarvesterTest {
    private Worker harvester;

    @BeforeAll
    public static void beforeAll() {
        Caches.PLANT_DEFS.intern(new PlantDef("planty", "mc plant", 23, 50, 10));
        Caches.WORKER_DEFS.intern(new WorkerDef("harvester", 1));
    }

    @BeforeEach
    public void beforeEach() {
        harvester = new Harvester();
    }

    @Test
    public void testHarvestPlot() {
        final Plant plant = new Plant(Caches.PLANT_DEFS.get("planty"), 100);
        final Plot plot = new Plot(plant, 50);

        harvester.work(plot);

        assertTrue(Player.getInstance().getWallet().getBalance() > 0);
    }
}
